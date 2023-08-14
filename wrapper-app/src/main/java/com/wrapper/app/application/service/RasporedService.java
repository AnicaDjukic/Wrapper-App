package com.wrapper.app.application.service;

import com.wrapper.app.infrastructure.dto.generator.MeetingDto;
import com.wrapper.app.domain.model.Database;
import com.wrapper.app.infrastructure.dto.optimizator.MeetingAssignment;
import com.wrapper.app.infrastructure.external.ConverterService;
import com.wrapper.app.infrastructure.external.OptimizatorService;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    private final ConverterService converterService;

    public RasporedService(DatabaseService databaseService, MeetingService meetingService, OptimizatorService optimizatorService, ConverterService converterService) {
        this.databaseService = databaseService;
        this.meetingService = meetingService;
        this.optimizatorService = optimizatorService;
        this.converterService = converterService;
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

    public void finishGenerating(List<MeetingAssignment> meetingAssignments) {
        Database database = databaseService.getUnfinished();
        database.setGenerationFinished(getLocalDate());
        CompletableFuture.runAsync(() -> converterService.convert(meetingAssignments, database));
        databaseService.update(database);

    }

    private Date getLocalDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
