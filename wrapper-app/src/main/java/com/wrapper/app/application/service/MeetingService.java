package com.wrapper.app.application.service;

import com.wrapper.app.domain.model.*;
import com.wrapper.app.infrastructure.dto.generator.*;
import com.wrapper.app.infrastructure.external.GeneratorService;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameProvider;
import com.wrapper.app.infrastructure.persistence.util.CollectionTypes;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class MeetingService {

    private final MongoTemplate mongoTemplate;

    private final ModelMapper modelMapper;

    private final GeneratorService generatorService;

    public MeetingService(MongoTemplate mongoTemplate, ModelMapper modelMapper, GeneratorService generatorService) {
        this.mongoTemplate = mongoTemplate;
        this.modelMapper = modelMapper;
        this.generatorService = generatorService;
    }

    public List<MeetingDto> createMeetings(Database database) throws IOException, InterruptedException {
        List<MeetingDto> meetingsDtos = generatorService.generateMeetings(database);
        saveMeetings(database, meetingsDtos);
        return meetingsDtos;
    }

    private void saveMeetings(Database database, List<MeetingDto> meetingsDtos) {
//        List<Meeting> meetings = meetingsDtos.parallelStream()
//                .map(dto -> CompletableFuture.supplyAsync(() -> {
//                    CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
//                    return modelMapper.map(dto, Meeting.class);
//                }))
//                .map(CompletableFuture::join)
//                .collect(Collectors.toList());
        List<Meeting> meetings = meetingsDtos.stream().map(m -> modelMapper.map(m, Meeting.class)).toList();
        String collectionName = CollectionTypes.MEETINGS + database.getGodina() + database.getSemestar().charAt(0);
        mongoTemplate.dropCollection(collectionName);
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(meetings,collectionName);
    }

    public List<Meeting> getMeetings(Database database) {
        String collectionName = CollectionTypes.MEETINGS + database.getGodina() + database.getSemestar().charAt(0);
        return mongoTemplate.findAll(Meeting.class, collectionName);
    }
}
