package com.wrapper.app.application.service;

import com.sun.mail.iap.ConnectionException;
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
import java.net.ConnectException;
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

    public void startGenerating(String id) {
        updateDatabaseStatus(id);
        CompletableFuture.runAsync(() -> createMeetingsAndStartOptimizator(id)).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    private void updateDatabaseStatus(String databaseId) {
        Database database = databaseService.getById(databaseId);
        database.setGenerationStarted(DateHandler.getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        database.setStatus(GenerationStatus.STARTED);
        databaseService.update(database);
    }

    private void createMeetingsAndStartOptimizator(String databaseId) {
        try {
            List<MeetingDto> meetings = createMeetings(databaseId);
            startOptimizator(databaseId, meetings);
        } catch (IOException | InterruptedException ex) {
            Database database = databaseService.getById(databaseId);
            database.setStatus(GenerationStatus.FAILED);
            databaseService.update(database);
            ex.printStackTrace();
        }
    }

    private List<MeetingDto> createMeetings(String databaseId) throws IOException, InterruptedException {
        List<MeetingDto> meetings = null;
        if (generationIsNotStopped(databaseId)) {
            Database database = databaseService.getById(databaseId);
            meetings = meetingService.createMeetings(database);
        }
        return meetings;
    }

    private void startOptimizator(String databaseId, List<MeetingDto> meetings) {
        Database database = databaseService.getById(databaseId);
        if(generationIsNotStopped(databaseId)) {
            optimizatorService.startOptimizator(database, meetings);
            if(generationIsNotStopped(databaseId)) {
                database.setStatus(GenerationStatus.OPTIMIZING);
                databaseService.update(database);
            } else {
                optimizatorService.stopOptimizator();
            }
        }
    }

    private boolean generationIsNotStopped(String databaseId) {
        return !databaseService.getById(databaseId).getStatus().equals(GenerationStatus.STOPPED);
    }

    public void sendSchedule(String email, String id) {
        Database database = databaseService.getById(id);
        emailSender.sendEmail(List.of(email), "Raspoed " + database.getSemestar() + database.getGodina(), "", database.getPath());
    }

    public void stopGenerating() {
        Database database = databaseService.getUnfinished();
        if(database.getStatus().equals(GenerationStatus.OPTIMIZING)) {
            CompletableFuture.runAsync(optimizatorService::stopOptimizator);
        }
        database.setStatus(GenerationStatus.STOPPED);
        databaseService.update(database);
    }

    public void finishGenerating(List<MeetingAssignment> meetingAssignments) {
        Database database = databaseService.getRecentlyStarted();
        if(database.getStatus().equals(GenerationStatus.OPTIMIZING)) {
            CompletableFuture.runAsync(() -> converterService.convert(meetingAssignments, database));
        }
    }
}
