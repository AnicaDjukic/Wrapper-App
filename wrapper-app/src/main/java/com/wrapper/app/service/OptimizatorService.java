package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.MeetingDto;
import com.wrapper.app.dto.optimizator.Meeting;
import com.wrapper.app.dto.optimizator.MeetingSchedule;
import com.wrapper.app.dto.optimizator.Semestar;
import com.wrapper.app.repository.util.CollectionNameProvider;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OptimizatorService {

    private final MongoTemplate mongoTemplate;

    private final RestTemplate restTemplate;

    private final ModelMapper modelMapper;

    private static final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private static final String PROSTORIJE = "Prostorije";
    private static final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private static final String PREDAVACI = "Predavaci";

    private static final String SEND_DATA_URL = "http://localhost:8081/timeTable";
    private static final String START_OPTIMIZATOR = "http://localhost:8081/timeTable/solve";

    public OptimizatorService(MongoTemplate mongoTemplate, RestTemplate restTemplate, ModelMapper modelMapper) {
        this.mongoTemplate = mongoTemplate;
        this.restTemplate = restTemplate;
        this.modelMapper = modelMapper;
    }

    public void startOptimizator(Database database, List<MeetingDto> meetings) {
        sendDataToOptimizator(database, meetings);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        restTemplate.exchange(START_OPTIMIZATOR, HttpMethod.POST, httpEntity, Void.class);
    }

    private void sendDataToOptimizator(Database database, List<MeetingDto> meetings) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MeetingSchedule meetingSchedule = createMeetingShedule(database, meetings);
        HttpEntity<MeetingSchedule> httpEntity = new HttpEntity<>(meetingSchedule, headers);
        restTemplate.exchange(SEND_DATA_URL, HttpMethod.POST, httpEntity, Void.class);
    }

    private MeetingSchedule createMeetingShedule(Database database, List<MeetingDto> meetingDtos) {
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
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
}
