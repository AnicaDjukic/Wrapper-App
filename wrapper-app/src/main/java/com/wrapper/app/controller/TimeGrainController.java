package com.wrapper.app.controller;

import com.wrapper.app.domain.TimeGrain;
import com.wrapper.app.repository.TimeGrainRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/time-grains")
public class TimeGrainController {

    private final TimeGrainRepository repository;

    public TimeGrainController(TimeGrainRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TimeGrain> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public TimeGrain getById(@PathVariable String id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }
}
