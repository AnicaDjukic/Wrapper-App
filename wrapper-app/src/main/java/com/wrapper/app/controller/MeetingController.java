package com.wrapper.app.controller;

import com.wrapper.app.domain.Meeting;
import com.wrapper.app.repository.MeetingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/meetings")
public class MeetingController {

    private final MeetingRepository repository;

    public MeetingController(MeetingRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Meeting> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Meeting getById(@PathVariable String id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }

}
