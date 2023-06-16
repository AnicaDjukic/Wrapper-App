package com.wrapper.app.service;

import com.google.gson.Gson;
import com.wrapper.app.domain.Database;
import com.wrapper.app.domain.Realizacija;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RasporedService {

    private final DatabaseService databaseService;

    private final MongoTemplate mongoTemplate;

    private static final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";

    public RasporedService(DatabaseService databaseService, MongoTemplate mongoTemplate) {
        this.databaseService = databaseService;
        this.mongoTemplate = mongoTemplate;
    }

    public void startGenerating(String id) {
        Database database = databaseService.getById(id);
        Realizacija realizacija = createRealizacija(database);
        try {
            realizacija.setId(UUID.randomUUID().toString());
            Realizacija updatedRealizacija = callPythonScript(realizacija);
            System.out.println(updatedRealizacija);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        database.setGenerationStarted(new Date());
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
}
