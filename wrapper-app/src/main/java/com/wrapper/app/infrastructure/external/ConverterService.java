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
import com.wrapper.app.infrastructure.util.FileExtensions;
import com.wrapper.app.infrastructure.util.FileHandler;
import com.wrapper.app.infrastructure.util.PythonScriptExecutor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${converter.script.path}")
    private String scriptPath;
    @Value("${converter.venv.path}")
    private String venvPath;

    private static final String MEETING_ASSIGNMENTS_PROP_NAME = "meetingAssignmentList";
    private static final String SCHEDULE_PROP_NAME = "schedule";
    private static final String RASPORED_PRIKAZ_PROP_NAME = "raspored_prikaz";
    private static final String FOLDER_PATH_PROP_NAME = "path";
    private static final String JSON_FILE_NAME = "data";

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
            ExecutionResult executionResult = scriptExecutor.executeScriptAndGetOutput(jsonFilePath, scriptPath, venvPath);

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
        inputData.add(MEETING_ASSIGNMENTS_PROP_NAME, JsonParser.parseString(objectMapper.writeValueAsString(meetingAssignmentDtos)));

        List<Meeting> meetings = meetingService.getMeetings(database);
        MeetingSchedule meetingSchedule = meetingScheduleService.createMeetingShedule(database, meetings, meetingAssignments);
        MeetingScheduleDto meetingScheduleDto = modelMapper.map(meetingSchedule, MeetingScheduleDto.class);
        inputData.add(SCHEDULE_PROP_NAME, JsonParser.parseString(objectMapper.writeValueAsString(meetingScheduleDto)));

        List<RasporedPrikaz> rasporedPrikaz = meetingScheduleService.getRasporedPrikaz();
        inputData.add(RASPORED_PRIKAZ_PROP_NAME, JsonParser.parseString(objectMapper.writeValueAsString(rasporedPrikaz)));

        String folderPath = fileHandler.createFiles(database.getGodina().replace("/", "_") + database.getSemestar());
        inputData.addProperty(FOLDER_PATH_PROP_NAME, folderPath + File.separator);

        String jsonInput = inputData.toString();
        File tempFile = File.createTempFile(JSON_FILE_NAME, FileExtensions.JSON);
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(jsonInput);
        }
        return tempFile.getAbsolutePath();
    }

    private void sendConvertedSchedule(Database database) {
        String folderName = database.getGodina().replace("/", "_") + database.getSemestar();
        String zipFolderName = database.getGodina().replace("/", "_") + database.getSemestar() + FileExtensions.ZIP;
        String zipPath = fileHandler.zipFolder(folderName, zipFolderName);
        notificationService.sendNotification(zipPath);
    }
}
