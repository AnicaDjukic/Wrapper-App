package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.*;
import com.wrapper.app.util.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RasporedService {

    private final DatabaseService databaseService;

    private final MeetingService meetingService;

    private final OptimizatorService optimizatorService;

    private final FileHandler fileHandler;

    private static final String LOCAL_PATH = "src/main/resources/files/";

    public RasporedService(DatabaseService databaseService, MeetingService meetingService, FileHandler fileHandler, MongoTemplate mongoTemplate, RestTemplate restTemplate, ModelMapper modelMapper, OptimizatorService optimizatorService) {
        this.databaseService = databaseService;
        this.meetingService = meetingService;
        this.fileHandler = fileHandler;
        this.optimizatorService = optimizatorService;
    }

    public void startGenerating(String id) {
        Database database = databaseService.getById(id);
        CompletableFuture.runAsync(() -> generateMeetingsAndStartOptimizator(database));
        database.setGenerationStarted(getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        databaseService.update(database);
    }

    private void generateMeetingsAndStartOptimizator(Database database) {
        try {
            List<MeetingDto> meetings = meetingService.generateMeetings(database);
            optimizatorService.startOptimizator(database, meetings);
        } catch (IOException ex) {
            database.setGenerationStarted(null);
            databaseService.update(database);
            ex.printStackTrace();
        }
    }

    public void finish(String id, MultipartFile raspored) {
        Database database = databaseService.getById(id);
        String filename = database.getGodina().replace("/", "_") + "_" + database.getSemestar() + ".xlsx";
        File rasporedFile = fileHandler.saveFile(raspored, LOCAL_PATH + filename);
        database.setGenerationFinished(getLocalDate());
        database.setPath(filename);
        databaseService.update(database);
    }

    private Date getLocalDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public Resource download(String filename) {
        return fileHandler.download(filename);
    }
}
