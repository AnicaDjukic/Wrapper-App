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
import com.wrapper.app.infrastructure.util.EmailSender;
import com.wrapper.app.infrastructure.util.FileHandler;
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

    private final FileHandler fileHandler;

    private final EmailSender emailSender;

    public ConverterService(ObjectMapper objectMapper,
                            ModelMapper modelMapper,
                            MeetingService meetingService,
                            MeetingScheduleService meetingScheduleService,
                            FileHandler fileHandler, EmailSender emailSender) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.meetingService = meetingService;
        this.meetingScheduleService = meetingScheduleService;
        this.fileHandler = fileHandler;
        this.emailSender = emailSender;
    }

    public void convert(List<MeetingAssignment> meetingAssignments, Database database) {
        try {

            String pythonScript = "src/main/resources/scripts/prikaz.py";
            String virtualEnvPython = "C:/Venv/venv/Scripts/python";

            JsonObject inputData = new JsonObject();
            List<MeetingAssignmentDto> meetingAssignmentDtos = meetingAssignments.stream().map(m -> modelMapper.map(m, MeetingAssignmentDto.class)).toList();
            inputData.add("meetingAssignmentList", JsonParser.parseString(objectMapper.writeValueAsString(meetingAssignmentDtos)));

            List<Meeting> meetings = meetingService.getMeetings(database);
            MeetingSchedule meetingSchedule = meetingScheduleService.createMeetingShedule(database, meetings, meetingAssignments);
            MeetingScheduleDto meetingScheduleDto = modelMapper.map(meetingSchedule, MeetingScheduleDto.class);
            inputData.add("schedule", JsonParser.parseString(objectMapper.writeValueAsString(meetingScheduleDto)));

            List<RasporedPrikaz> rasporedPrikaz = meetingScheduleService.getRasporedPrikaz();
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

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                String zipPath = fileHandler.zipFolder(database.getGodina().replace("/", "_") + database.getSemestar(), database.getGodina().replace("/", "_") + database.getSemestar() + ".zip");
                emailSender.sendEmail("wrapper.app@outlook.com", "Raspored", "", zipPath);
            }
            System.out.println(output);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
