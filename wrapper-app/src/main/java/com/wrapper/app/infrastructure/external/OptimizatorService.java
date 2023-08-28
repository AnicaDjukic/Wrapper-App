package com.wrapper.app.infrastructure.external;

import com.wrapper.app.infrastructure.dto.generator.MeetingDto;
import com.wrapper.app.application.service.MeetingScheduleService;
import com.wrapper.app.domain.model.Database;
import com.wrapper.app.domain.model.Meeting;
import com.wrapper.app.domain.model.MeetingSchedule;
import com.wrapper.app.infrastructure.dto.optimizator.MeetingScheduleDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${optimizator.url}")
    private String optimizatorUrl;

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
        String startUrl = optimizatorUrl + "/timeTable/solve";
        restTemplate.exchange(startUrl, HttpMethod.POST, httpEntity, Void.class);
    }

    private void sendDataToOptimizator(Database database, List<MeetingDto> meetingDtos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Meeting> meetings = meetingDtos.stream().map(m -> modelMapper.map(m, Meeting.class)).toList();
        MeetingSchedule meetingSchedule = meetingScheduleService.createMeetingShedule(database, meetings, null);
        MeetingScheduleDto meetingScheduleDto = modelMapper.map(meetingSchedule, MeetingScheduleDto.class);
        HttpEntity<MeetingScheduleDto> httpEntity = new HttpEntity<>(meetingScheduleDto, headers);
        String sendDataUrl = optimizatorUrl + "/timeTable";
        restTemplate.exchange(sendDataUrl, HttpMethod.POST, httpEntity, Void.class);
    }

    public void stopOptimizator() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(null, headers);
        String stopUrl = optimizatorUrl + "/timeTable/stopSolving";
        restTemplate.exchange(stopUrl, HttpMethod.POST, httpEntity, Void.class);
    }
}
