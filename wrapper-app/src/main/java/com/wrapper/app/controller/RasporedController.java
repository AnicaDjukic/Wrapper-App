package com.wrapper.app.controller;

import com.wrapper.app.dto.optimizator.MeetingAssignment;
import com.wrapper.app.service.RasporedService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/raspored")
public class RasporedController {

    private final RasporedService service;

    public RasporedController(RasporedService service) {
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
