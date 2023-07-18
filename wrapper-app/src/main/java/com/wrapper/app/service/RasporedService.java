package com.wrapper.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.*;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.util.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RasporedService {

    private final DatabaseService databaseService;

    private final MongoTemplate mongoTemplate;

    private final FileHandler fileHandler;

    private final ModelMapper modelMapper;

    private static final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";
    private static final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private static final String PROSTORIJE = "Prostorije";
    private static final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private static final String PREDMETI = "Predmeti";
    private static final String PREDAVACI = "Predavaci";
    private static final String LOCAL_PATH = "src/main/resources/files/";

    public RasporedService(DatabaseService databaseService, MongoTemplate mongoTemplate, FileHandler fileHandler, ModelMapper modelMapper) {
        this.databaseService = databaseService;
        this.mongoTemplate = mongoTemplate;
        this.fileHandler = fileHandler;
        this.modelMapper = modelMapper;
    }

    public List<MeetingDto> startGenerating(String id) {
        Database database = databaseService.getById(id);
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
        RealizacijaDto realizacija = createRealizacija(database);
        List<StudijskiProgramDto> studijskiProgrami = getStudijskiProgrami(database);
        List<ProstorijaDto> prostorije = getProstorije(database);
        List<StudentskaGrupaDto> studentskeGrupe = getStudentskeGrupe(database);
        List<PredmetDto> predmeti = getPredmeti(database);
        List<PredavacDto> predavaci = getPredavaci(database);
        List<MeetingDto> updatedRealizacija = null;
        try {
            // TODO: ovde umesto realizacije ce se dobiti lista Meeting objekata
            updatedRealizacija = callPythonScript(realizacija,
                    studijskiProgrami, prostorije, studentskeGrupe, predmeti, predavaci);
            // TODO: pozvati jos 2 endpointa
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        database.setGenerationStarted(getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        databaseService.update(database);
        return updatedRealizacija;
    }

    private RealizacijaDto createRealizacija(Database database) {
        RealizacijaDto realizacija = new RealizacijaDto();
        realizacija.setGodina(database.getGodina());
        realizacija.setSemestar(database.getSemestar().substring(0, 1));
        String collectionName = STUDIJSKI_PROGRAM_PREDMETI + database.getGodina() + database.getSemestar().charAt(0);
        List<StudijskiProgramPredmeti> studijskiProgramPredmeti = mongoTemplate.findAll(StudijskiProgramPredmeti.class, collectionName);
        List<StudijskiProgramPredmetiDto> studijskiProgramPredmetiDtos = getStudijskiProgramPredmetiDtos(studijskiProgramPredmeti);
        realizacija.setStudijskiProgramPredmeti(studijskiProgramPredmetiDtos);
        return realizacija;
    }

    private List<StudijskiProgramPredmetiDto> getStudijskiProgramPredmetiDtos(List<StudijskiProgramPredmeti> studijskiProgramPredmeti) {
        List<StudijskiProgramPredmetiDto> studijskiProgramPredmetiDtos = new ArrayList<>();
        for (StudijskiProgramPredmeti studijskiProgramPredmet : studijskiProgramPredmeti) {
            StudijskiProgramPredmetiDto studijskiProgramPredmetiDto = new StudijskiProgramPredmetiDto();
            studijskiProgramPredmetiDto.setStudijskiProgramId(studijskiProgramPredmet.getStudijskiProgram().getId());
            List<PredmetPredavacDto> predmetPredavacDtos = getPredmetPredavacDtos(studijskiProgramPredmet.getPredmetPredavaci());
            studijskiProgramPredmetiDto.setPredmetPredavaci(predmetPredavacDtos);
            studijskiProgramPredmetiDtos.add(studijskiProgramPredmetiDto);
        }
        return studijskiProgramPredmetiDtos;
    }

    private List<PredmetPredavacDto> getPredmetPredavacDtos(List<PredmetPredavac> predmetPredavaci) {
        List<PredmetPredavacDto> predmetPredavacDtos = new ArrayList<>();
        for (PredmetPredavac predmetPredavac : predmetPredavaci) {
            PredmetPredavacDto predmetPredavacDto = new PredmetPredavacDto();
            predmetPredavacDto.setPredmetId(predmetPredavac.getPredmet().getId());
            predmetPredavacDto.setPredmetPlan(predmetPredavac.getPredmet().getPlan());
            predmetPredavacDto.setPredmetGodina(predmetPredavac.getPredmet().getGodina());
            predmetPredavacDto.setPredmetOznaka(predmetPredavac.getPredmet().getOznaka());
            if(predmetPredavac.getProfesor() != null) {
                predmetPredavacDto.setProfesorId(predmetPredavac.getProfesor().getId());
            }
            predmetPredavac.getOstaliProfesori().forEach(p -> predmetPredavacDto.getOstaliProfesori().add(p.getId()));
            predmetPredavac.getAsistentZauzeca().forEach(a -> {
                AsistentZauzecaDto dto = new AsistentZauzecaDto();
                dto.setAsistentId(a.getAsistent().getId());
                dto.setBrojTermina(a.getBrojTermina());
                predmetPredavacDto.getAsistentZauzeca().add(dto);
            });
            predmetPredavacDtos.add(predmetPredavacDto);
        }
        return predmetPredavacDtos;
    }

    private List<StudijskiProgramDto> getStudijskiProgrami(Database database) {
        String collectionName = STUDIJSKI_PROGRAMI + database.getGodina() + database.getSemestar().charAt(0);
        List<StudijskiProgram> studijskiProgrami = mongoTemplate.findAll(StudijskiProgram.class, collectionName);
        return studijskiProgrami.stream().map(s -> modelMapper.map(s, StudijskiProgramDto.class)).toList();
    }

    private List<ProstorijaDto> getProstorije(Database database) {
        String collectionName = PROSTORIJE + database.getGodina() + database.getSemestar().charAt(0);
        List<Prostorija> prostorije = mongoTemplate.findAll(Prostorija.class, collectionName);
        return prostorije.stream().map(s -> modelMapper.map(s, ProstorijaDto.class)).toList();
    }

    private List<StudentskaGrupaDto> getStudentskeGrupe(Database database) {
        String collectionName = STUDENTSKE_GRUPE + database.getGodina() + database.getSemestar().charAt(0);
        List<StudentskaGrupa> studentskeGrupe = mongoTemplate.findAll(StudentskaGrupa.class, collectionName);
        return studentskeGrupe.stream().map(s -> modelMapper.map(s, StudentskaGrupaDto.class)).toList();
    }

    private List<PredmetDto> getPredmeti(Database database) {
        String collectionName = PREDMETI + database.getGodina() + database.getSemestar().charAt(0);
        List<Predmet> predmeti = mongoTemplate.findAll(Predmet.class, collectionName);
        return predmeti.stream().map(p -> modelMapper.map(p, PredmetDto.class)).toList();
    }

    private List<PredavacDto> getPredavaci(Database database) {
        String collectionName = PREDAVACI + database.getGodina() + database.getSemestar().charAt(0);
        List<Predavac> predavaci = mongoTemplate.findAll(Predavac.class, collectionName);
        return predavaci.stream().map(p -> modelMapper.map(p, PredavacDto.class)).toList();
    }

    private List<MeetingDto> callPythonScript(RealizacijaDto realizacija,
                                            List<StudijskiProgramDto> studijskiProgramList,
                                            List<ProstorijaDto> prostorije,
                                            List<StudentskaGrupaDto> studentskaGrupaList,
                                            List<PredmetDto> predmetList,
                                            List<PredavacDto> predavacList) throws IOException {
        // Specify the path to the Python script
        String pythonScriptPath = "src/main/resources/scripts/7_generate_termini.py";

        // Create a JSON object that contains the `realizacija` and `studijskiProgramList`
        ObjectMapper objectMapper = new ObjectMapper();
        JsonObject inputData = new JsonObject();
        inputData.add("realizacija", JsonParser.parseString(objectMapper.writeValueAsString(realizacija)));
        inputData.add("studijskiProgramList", JsonParser.parseString(objectMapper.writeValueAsString(studijskiProgramList)));
        inputData.add("studentskaGrupaList", JsonParser.parseString(objectMapper.writeValueAsString(studentskaGrupaList)));
        inputData.add("prostorijaList", JsonParser.parseString(objectMapper.writeValueAsString(prostorije)));
        inputData.add("predmetList", JsonParser.parseString(objectMapper.writeValueAsString(predmetList)));
        inputData.add("predavacList", JsonParser.parseString(objectMapper.writeValueAsString(predavacList)));

        // Convert the JSON object to a string
        String jsonInput = inputData.toString();

        // Write the JSON data to a temporary file
        File tempFile = File.createTempFile("data", ".json");
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(jsonInput);
        }

        // Specify the path to the Python script and the JSON file
        String jsonFilePath = tempFile.getAbsolutePath();

        // Construct the command to execute the Python script
        String[] command = {"python", pythonScriptPath, jsonFilePath};

        // Create a ProcessBuilder instance with the command
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        // Redirect the process's output to a pipe
        processBuilder.redirectErrorStream(true);

        // Start the process
        Process process = processBuilder.start();

        // Read the output from the Python script
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder outputBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            outputBuilder.append(line);
        }
        String jsonOutput = outputBuilder.toString();

        // Close the BufferedReader
        reader.close();

        // Print the output for verification
        System.out.println("JSON Output: " + jsonOutput);

        // Deserialize the JSON output into a RealizacijaDto object
        List<MeetingDto> meetings = objectMapper.readValue(jsonOutput, new TypeReference<>(){});
        // Return the updated RealizacijaDto object
        return meetings;
    }

    private Date getLocalDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void finish(String id, MultipartFile raspored) {
        Database database = databaseService.getById(id);
        String filename = database.getGodina().replace("/", "_") + "_" + database.getSemestar() + ".xlsx";
        File rasporedFile = fileHandler.saveFile(raspored, LOCAL_PATH + filename);
        database.setGenerationFinished(getLocalDate());
        database.setPath(filename);
        databaseService.update(database);
    }

    public Resource download(String filename) {
        return fileHandler.download(filename);
    }
}
