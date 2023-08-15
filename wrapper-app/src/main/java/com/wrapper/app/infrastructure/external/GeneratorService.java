package com.wrapper.app.infrastructure.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.app.domain.model.*;
import com.wrapper.app.infrastructure.dto.generator.*;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameProvider;
import com.wrapper.app.infrastructure.util.ExecutionResult;
import com.wrapper.app.infrastructure.util.PythonScriptExecutor;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class GeneratorService {

    private final ObjectMapper objectMapper;

    private final PythonScriptExecutor scriptExecutor;

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;

    private static final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";
    private static final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private static final String PROSTORIJE = "Prostorije";
    private static final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private static final String PREDMETI = "Predmeti";
    private static final String PREDAVACI = "Predavaci";

    public GeneratorService(ObjectMapper objectMapper, PythonScriptExecutor scriptExecutor, MongoTemplate mongoTemplate, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.scriptExecutor = scriptExecutor;
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
    }

    public List<MeetingDto> generateMeetings(Database database) throws IOException, InterruptedException {
        String jsonFilePath = prepareData(database);
        String pythonScriptPath = "src/main/resources/scripts/7_generate_termini.py";
        String virtualEnvPython = "python";
        ExecutionResult executionResult = scriptExecutor.executeScriptAndGetOutput(jsonFilePath, pythonScriptPath, virtualEnvPython);
        return objectMapper.readValue(executionResult.getScriptOutput(), new TypeReference<>() {});
    }

    private String prepareData(Database database) throws IOException {
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
        JsonObject inputData = new JsonObject();
        inputData.add("realizacija", JsonParser.parseString(objectMapper.writeValueAsString(createRealizacija(database))));
        inputData.add("studijskiProgramList", JsonParser.parseString(objectMapper.writeValueAsString(getStudijskiProgrami(database))));
        inputData.add("studentskaGrupaList", JsonParser.parseString(objectMapper.writeValueAsString(getStudentskeGrupe(database))));
        inputData.add("prostorijaList", JsonParser.parseString(objectMapper.writeValueAsString(getProstorije(database))));
        inputData.add("predmetList", JsonParser.parseString(objectMapper.writeValueAsString(getPredmeti(database))));
        inputData.add("predavacList", JsonParser.parseString(objectMapper.writeValueAsString(getPredavaci(database))));

        String jsonInput = inputData.toString();
        File tempFile = File.createTempFile("data", ".json");
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(jsonInput);
        }
        return tempFile.getAbsolutePath();
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
        return studijskiProgramPredmeti.stream().map(sp -> modelMapper.map(sp, StudijskiProgramPredmetiDto.class)).toList();
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
}
