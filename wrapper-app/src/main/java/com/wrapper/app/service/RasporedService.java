package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.*;
import com.wrapper.app.dto.optimizator.Meeting;
import com.wrapper.app.dto.optimizator.MeetingSchedule;
import com.wrapper.app.dto.optimizator.Semestar;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.util.FileHandler;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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

@Service
public class RasporedService {

    private final DatabaseService databaseService;

    private final MeetingService meetingService;

    private final FileHandler fileHandler;

    private final MongoTemplate mongoTemplate;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private static final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private static final String PROSTORIJE = "Prostorije";
    private static final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private static final String PREDAVACI = "Predavaci";

    private static final String LOCAL_PATH = "src/main/resources/files/";

    public RasporedService(DatabaseService databaseService, MeetingService meetingService, FileHandler fileHandler, MongoTemplate mongoTemplate, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.databaseService = databaseService;
        this.meetingService = meetingService;
        this.fileHandler = fileHandler;
        this.mongoTemplate = mongoTemplate;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    public MeetingSchedule startGenerating(String id) {
        Database database = databaseService.getById(id);
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
        List<MeetingDto> meetings = null;
        try {
            meetings = meetingService.generateMeetings(database);
            sendDataToOptimizator(database, meetings);
            startOptimizator();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        database.setGenerationStarted(getLocalDate());
        database.setGenerationFinished(null);
        database.setPath(null);
        databaseService.update(database);
        return createMeetingShedule(database, meetings);
    }

    public void sendDataToOptimizator(Database database, List<MeetingDto> meetings) {
        String apiUrl = "http://localhost:8081/timeTable";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MeetingSchedule meetingSchedule = createMeetingShedule(database, meetings);
        HttpEntity<MeetingSchedule> httpEntity = new HttpEntity<>(meetingSchedule, headers);
        restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, Void.class);
    }

    private void startOptimizator() {
        String apiUrl = "http://localhost:8081/timeTable/solve";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, Void.class);
    }

    private MeetingSchedule createMeetingShedule(Database database, List<MeetingDto> meetingDtos) {
        List<Meeting> meetings = meetingDtos.stream().map(m -> modelMapper.map(m, Meeting.class)).toList();
        return new MeetingSchedule(
                getStudijskiProgrami(database),
                meetings,
                getDani(),
                getTimeGrains(),
                getProstorije(database),
                getPredavaci(database),
                getStudentskeGrupe(database),
                Semestar.valueOf(database.getSemestar().substring(0, 1))
        );
    }

    private List<StudijskiProgram> getStudijskiProgrami(Database database) {
        String collectionName = STUDIJSKI_PROGRAMI + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(StudijskiProgram.class, collectionName);
    }

    private List<Dan> getDani() {
        return mongoTemplate.findAll(Dan.class, Dan.class.getSimpleName());
    }

    private List<TimeGrain> getTimeGrains() {
        return mongoTemplate.findAll(TimeGrain.class, TimeGrain.class.getSimpleName());
    }

    private List<Prostorija> getProstorije(Database database) {
        String collectionName = PROSTORIJE + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(Prostorija.class, collectionName);
    }

    private List<Predavac> getPredavaci(Database database) {
        String collectionName = PREDAVACI + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(Predavac.class, collectionName);
    }

    private List<StudentskaGrupa> getStudentskeGrupe(Database database) {
        String collectionName = STUDENTSKE_GRUPE + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(StudentskaGrupa.class, collectionName);
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
