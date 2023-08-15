package com.wrapper.app.infrastructure.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wrapper.app.infrastructure.dto.converter.MeetingAssignmentDto;
import com.wrapper.app.infrastructure.dto.converter.MeetingScheduleDto;
import com.wrapper.app.infrastructure.dto.converter.RasporedPrikaz;
import com.wrapper.app.infrastructure.dto.optimizator.MeetingAssignment;
import com.wrapper.app.application.service.MeetingScheduleService;
import com.wrapper.app.application.service.MeetingService;
import com.wrapper.app.domain.model.Database;
import com.wrapper.app.domain.model.Meeting;
import com.wrapper.app.domain.model.MeetingSchedule;
import com.wrapper.app.infrastructure.util.ExecutionResult;
import com.wrapper.app.infrastructure.util.FileHandler;
import com.wrapper.app.infrastructure.util.PythonScriptExecutor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ConverterService {

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;

    private final MeetingService meetingService;

    private final MeetingScheduleService meetingScheduleService;

    private final PythonScriptExecutor scriptExecutor;

    private final FileHandler fileHandler;

    private final NotificationService notificationService;

    public ConverterService(ObjectMapper objectMapper,
                            ModelMapper modelMapper,
                            MeetingService meetingService,
                            MeetingScheduleService meetingScheduleService,
                            PythonScriptExecutor scriptExecutor,
                            FileHandler fileHandler, NotificationService notificationService) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.meetingService = meetingService;
        this.meetingScheduleService = meetingScheduleService;
        this.scriptExecutor = scriptExecutor;
        this.fileHandler = fileHandler;
        this.notificationService = notificationService;
    }

    public void convert(List<MeetingAssignment> meetingAssignments, Database database) {
        try {
            String jsonFilePath = prepareData(meetingAssignments, database);
            String pythonScript = "src/main/resources/scripts/prikaz.py";
            String virtualEnvPython = "C:/Venv/venv/Scripts/python";
            ExecutionResult executionResult = scriptExecutor.executeScriptAndGetOutput(jsonFilePath, pythonScript, virtualEnvPython);

            if (executionResult.getExitCode() == 0) {
                sendConvertedSchedule(database);
            }
            System.out.println(executionResult.getScriptOutput());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String prepareData(List<MeetingAssignment> meetingAssignments, Database database) throws IOException {
        JsonObject inputData = new JsonObject();
        List<MeetingAssignmentDto> meetingAssignmentDtos = meetingAssignments.stream().map(m -> modelMapper.map(m, MeetingAssignmentDto.class)).toList();
        inputData.add("meetingAssignmentList", JsonParser.parseString(objectMapper.writeValueAsString(meetingAssignmentDtos)));

        List<Meeting> meetings = meetingService.getMeetings(database);
        MeetingSchedule meetingSchedule = meetingScheduleService.createMeetingShedule(database, meetings, meetingAssignments);
        MeetingScheduleDto meetingScheduleDto = modelMapper.map(meetingSchedule, MeetingScheduleDto.class);
        inputData.add("schedule", JsonParser.parseString(objectMapper.writeValueAsString(meetingScheduleDto)));

        List<RasporedPrikaz> rasporedPrikaz = meetingScheduleService.getRasporedPrikaz();
        inputData.add("raspored_prikaz", JsonParser.parseString(objectMapper.writeValueAsString(rasporedPrikaz)));

        String folderPath = fileHandler.createFiles(database.getGodina().replace("/", "_") + database.getSemestar());
        inputData.addProperty("path", folderPath + "/");

        String jsonInput = inputData.toString();
        File tempFile = File.createTempFile("data", ".json");
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(jsonInput);
        }
        return tempFile.getAbsolutePath();
    }

    private void sendConvertedSchedule(Database database) {
        String folderName = database.getGodina().replace("/", "_") + database.getSemestar();
        String zipFolderName = database.getGodina().replace("/", "_") + database.getSemestar() + ".zip";
        String zipPath = fileHandler.zipFolder(folderName, zipFolderName);
        notificationService.sendNotification(zipPath);
    }
}
