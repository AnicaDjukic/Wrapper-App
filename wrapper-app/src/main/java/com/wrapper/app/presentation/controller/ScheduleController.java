package com.wrapper.app.presentation.controller;

import com.wrapper.app.domain.model.Database;
import com.wrapper.app.domain.model.User;
import com.wrapper.app.infrastructure.dto.optimizator.MeetingAssignment;
import com.wrapper.app.application.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Database startGenerating(@PathVariable String id) {
        return service.startGenerating(id);
    }

    @PutMapping("/generate/stop")
    public void stopGenerating() {
        service.stopGenerating();
    }

    @PostMapping("/send/{id}")
    public void sendSchedule(@PathVariable String id, @AuthenticationPrincipal User user) {
        String email = user.getUsername();
        service.sendSchedule(email, id);
    }

    @PostMapping("/finish")
    @ResponseStatus(HttpStatus.OK)
    public void finishGenerating(@RequestBody List<MeetingAssignment> meetingAssignments) {
        service.finishGenerating(meetingAssignments);
    }

}
