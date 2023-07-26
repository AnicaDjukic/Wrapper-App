package com.wrapper.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.app.domain.Database;
import com.wrapper.app.domain.Meeting;
import com.wrapper.app.dto.converter.MeetingAssignmentDto;
import com.wrapper.app.dto.converter.MeetingScheduleDto;
import com.wrapper.app.dto.converter.RasporedPrikaz;
import com.wrapper.app.dto.optimizator.MeetingAssignment;
import com.wrapper.app.domain.MeetingSchedule;
import com.wrapper.app.util.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ConverterService {

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;

    private final MeetingService meetingService;

    private final MeetingScheduleService meetingScheduleService;

    private final MongoTemplate mongoTemplate;

    private final FileHandler fileHandler;

    private static final String RASPORED_PRIKAZ = "RasporedPrikaz";

    public ConverterService(ObjectMapper objectMapper, ModelMapper modelMapper, MeetingService meetingService, MeetingScheduleService meetingScheduleService, MongoTemplate mongoTemplate, FileHandler fileHandler) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.meetingService = meetingService;
        this.meetingScheduleService = meetingScheduleService;
        this.mongoTemplate = mongoTemplate;
        this.fileHandler = fileHandler;
    }

    public String convert(List<MeetingAssignment> meetingAssignments, Database database) {
        try {
            // Replace 'python_script.py' with the actual path to your Python script.
            String pythonScript = "src/main/resources/scripts/prikaz.py";
            String virtualEnvPython = "C:/Venv/venv/Scripts/python";

            JsonObject inputData = new JsonObject();
            List<MeetingAssignmentDto> meetingAssignmentDtos = meetingAssignments.stream().map(m -> modelMapper.map(m, MeetingAssignmentDto.class)).toList();
            inputData.add("meetingAssignmentList", JsonParser.parseString(objectMapper.writeValueAsString(meetingAssignmentDtos)));

            List<Meeting> meetings = meetingService.getMeetings(database);
            MeetingSchedule meetingSchedule = meetingScheduleService.createMeetingShedule(database, meetings, meetingAssignments);
            MeetingScheduleDto meetingScheduleDto = modelMapper.map(meetingSchedule, MeetingScheduleDto.class);
            inputData.add("schedule", JsonParser.parseString(objectMapper.writeValueAsString(meetingScheduleDto)));

            List<RasporedPrikaz> rasporedPrikaz = mongoTemplate.findAll(RasporedPrikaz.class, RASPORED_PRIKAZ);
            inputData.add("raspored_prikaz", JsonParser.parseString(objectMapper.writeValueAsString(rasporedPrikaz)));

            String folderPath = fileHandler.crateFolder(database.getGodina().replace("/", "_") + database.getSemestar());
            inputData.addProperty("path", folderPath + "/");

            String jsonInput = inputData.toString();
            File tempFile = File.createTempFile("data", ".json");
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                fileWriter.write(jsonInput);
            }
            String path = tempFile.getAbsolutePath();

            ProcessBuilder processBuilder = new ProcessBuilder(
                    virtualEnvPython, pythonScript, path
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Wait for the Python script to finish its execution.
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                fileHandler.zipFolder(database.getGodina().replace("/", "_") + database.getSemestar(), database.getGodina().replace("/", "_") + database.getSemestar() + ".zip");
                return "Python script executed successfully.\n" + output.toString();
            } else {
                return "Python script execution failed.\n" + output.toString();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error executing the Python script.";
        }
    }
}
