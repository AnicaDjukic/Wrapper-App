package com.wrapper.app.service;

import com.google.gson.Gson;
import com.wrapper.app.domain.Database;
import com.wrapper.app.domain.Realizacija;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import com.wrapper.app.util.FileHandler;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class RasporedService {

    private final DatabaseService databaseService;

    private final MongoTemplate mongoTemplate;

    private final FileHandler fileHandler;

    private static final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";
    private static final String LOCAL_PATH = "src/main/resources/files/";

    public RasporedService(DatabaseService databaseService, MongoTemplate mongoTemplate, FileHandler fileHandler) {
        this.databaseService = databaseService;
        this.mongoTemplate = mongoTemplate;
        this.fileHandler = fileHandler;
    }

    public void startGenerating(String id) {
        Database database = databaseService.getById(id);
        Realizacija realizacija = createRealizacija(database);
        try {
            realizacija.setId(database.getId());
            // TODO: ovde umesto realizacije ce se dobiti lista Meeting objekata
            Realizacija updatedRealizacija = callPythonScript(realizacija);
            // TODO: pozvati jos 2 endpointa
            System.out.println(updatedRealizacija);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        database.setGenerationStarted(getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        databaseService.update(database);
    }

    private Realizacija createRealizacija(Database database) {
        Realizacija realizacija = new Realizacija();
        realizacija.setGodina(database.getGodina());
        realizacija.setSemestar(database.getSemestar().substring(0, 1));
        String collectionName = STUDIJSKI_PROGRAM_PREDMETI + database.getGodina() + database.getSemestar().charAt(0);
        List<StudijskiProgramPredmeti> studijskiProgramPredmeti = mongoTemplate.findAll(StudijskiProgramPredmeti.class, collectionName);
        realizacija.setStudijskiProgramPredmeti(studijskiProgramPredmeti);
        return realizacija;
    }

    private Realizacija callPythonScript(Realizacija realizacija) throws IOException {
        // Specify the path to the Python script
        String pythonScriptPath = "src/main/resources/realizacija.py";

        // Write JSON data to a temporary file
        File tempFile = File.createTempFile("data", ".json");
        String jsonInput = toJson(realizacija);
        Files.write(tempFile.toPath(), jsonInput.getBytes());

        // Specify the path to the Python script and the JSON file
        String jsonFilePath = tempFile.getAbsolutePath();

        // Create a Process instance to execute the Python script
        Process process = Runtime.getRuntime().exec("python " + pythonScriptPath + " " + jsonFilePath);

        // Read the output JSON from the Python script
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String jsonOutput = reader.readLine();

        return fromJson(jsonOutput);
    }

    private static String toJson(Realizacija realizacija) {
        Gson gson = new Gson();
        return gson.toJson(realizacija);
    }

    private static Realizacija fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Realizacija.class);
    }

    private Date getLocalDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        // Convert LocalDateTime to Date
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
