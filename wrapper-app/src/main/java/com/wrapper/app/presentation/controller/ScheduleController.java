package com.wrapper.app.presentation.controller;

import com.wrapper.app.infrastructure.dto.optimizator.MeetingAssignment;
import com.wrapper.app.application.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/raspored")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @PostMapping("/generate/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void generate(@PathVariable String id) {
        service.startGenerating(id);
    }

    @PostMapping("/finish")
    @ResponseStatus(HttpStatus.OK)
    public void finishGenerating(@RequestBody List<MeetingAssignment> meetingAssignments) {
        service.finishGenerating(meetingAssignments);
    }

}
