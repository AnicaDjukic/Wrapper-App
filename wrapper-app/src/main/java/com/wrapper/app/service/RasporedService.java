package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.*;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.util.FileHandler;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class RasporedService {

    private final DatabaseService databaseService;

    private final MeetingService meetingService;

    private final FileHandler fileHandler;

    private static final String LOCAL_PATH = "src/main/resources/files/";

    public RasporedService(DatabaseService databaseService, MeetingService meetingService, FileHandler fileHandler) {
        this.databaseService = databaseService;
        this.meetingService = meetingService;
        this.fileHandler = fileHandler;
    }

    public List<MeetingDto> startGenerating(String id) {
        Database database = databaseService.getById(id);
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
        List<MeetingDto> meetings = null;
        try {
            meetings = meetingService.generateMeetings(database);
            // TODO: pozvati jos 2 endpointa
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        database.setGenerationStarted(getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        databaseService.update(database);
        return meetings;
    }

    private Date getLocalDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
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
