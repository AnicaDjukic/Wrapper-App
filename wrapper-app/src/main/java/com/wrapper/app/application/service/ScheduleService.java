package com.wrapper.app.application.service;

import com.wrapper.app.domain.model.GenerationStatus;
import com.wrapper.app.infrastructure.dto.generator.MeetingDto;
import com.wrapper.app.domain.model.Database;
import com.wrapper.app.infrastructure.dto.optimizator.MeetingAssignment;
import com.wrapper.app.infrastructure.external.ConverterService;
import com.wrapper.app.infrastructure.external.DatabaseService;
import com.wrapper.app.infrastructure.external.OptimizatorService;
import com.wrapper.app.infrastructure.util.DateHandler;
import com.wrapper.app.infrastructure.util.EmailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ScheduleService {

    private final DatabaseService databaseService;

    private final MeetingService meetingService;

    private final OptimizatorService optimizatorService;

    private final ConverterService converterService;

    private final EmailSender emailSender;

    public ScheduleService(DatabaseService databaseService, MeetingService meetingService,
                           OptimizatorService optimizatorService, ConverterService converterService,
                           EmailSender emailSender) {
        this.databaseService = databaseService;
        this.meetingService = meetingService;
        this.optimizatorService = optimizatorService;
        this.converterService = converterService;
        this.emailSender = emailSender;
    }

    public Database startGenerating(String id) {
        Database database = databaseService.getById(id);
        CompletableFuture.runAsync(() -> createMeetingsAndStartOptimizator(database)).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });;
        database.setGenerationStarted(DateHandler.getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        database.setStatus(GenerationStatus.STARTED);
        return databaseService.update(database);
    }

    private void createMeetingsAndStartOptimizator(Database database) {
        try {
            List<MeetingDto> meetings = meetingService.createMeetings(database);
            optimizatorService.startOptimizator(database, meetings);
            database.setStatus(GenerationStatus.OPTIMIZING);
            databaseService.update(database);
        } catch (IOException | InterruptedException ex) {
            database.setStatus(GenerationStatus.STOPPED); // TODO: STAVITI OVDE STANJE FAILED
            database.setGenerationStarted(null);
            databaseService.update(database);
            ex.printStackTrace();
        }
    }

    public void sendSchedule(String email, String id) {
        Database database = databaseService.getById(id);
        emailSender.sendEmail(List.of(email), "Raspoed " + database.getSemestar() + database.getGodina(), "", database.getPath());
    }

    public void stopGenerating() {
        Database database = databaseService.getUnfinished();
        database.setGenerationStarted(null);
        if(database.getStatus().equals(GenerationStatus.OPTIMIZING)) {
            CompletableFuture.runAsync(optimizatorService::stopOptimizator);
        }
        database.setStatus(GenerationStatus.STOPPED);
        database.setPath(null);
        databaseService.update(database);
    }

    public void finishGenerating(List<MeetingAssignment> meetingAssignments) {
        Database database = databaseService.getUnfinished();
        if(database.getStatus().equals(GenerationStatus.OPTIMIZING)) {
            CompletableFuture.runAsync(() -> converterService.convert(meetingAssignments, database));
        }
    }
}
