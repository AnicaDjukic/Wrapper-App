package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.MeetingDto;
import com.wrapper.app.domain.MeetingSchedule;
import com.wrapper.app.dto.optimizator.MeetingScheduleDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OptimizatorService {

    private final RestTemplate restTemplate;

    private final MeetingScheduleService meetingScheduleService;

    private final ModelMapper modelMapper;

    private static final String SEND_DATA_URL = "http://localhost:8081/timeTable";
    private static final String START_URL = "http://localhost:8081/timeTable/solve";

    public OptimizatorService(RestTemplate restTemplate, MeetingScheduleService meetingScheduleService, ModelMapper modelMapper) {
        this.restTemplate = restTemplate;
        this.meetingScheduleService = meetingScheduleService;
        this.modelMapper = modelMapper;
    }

    public void startOptimizator(Database database, List<MeetingDto> meetings) {
        sendDataToOptimizator(database, meetings);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        restTemplate.exchange(START_URL, HttpMethod.POST, httpEntity, Void.class);
    }

    private void sendDataToOptimizator(Database database, List<MeetingDto> meetingDtos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Meeting> meetings = meetingDtos.stream().map(m -> modelMapper.map(m, Meeting.class)).toList();
        MeetingSchedule meetingSchedule = meetingScheduleService.createMeetingShedule(database, meetings, null);
        MeetingScheduleDto meetingScheduleDto = modelMapper.map(meetingSchedule, MeetingScheduleDto.class);
        HttpEntity<MeetingScheduleDto> httpEntity = new HttpEntity<>(meetingScheduleDto, headers);
        restTemplate.exchange(SEND_DATA_URL, HttpMethod.POST, httpEntity, Void.class);
    }
}
