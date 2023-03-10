package com.wrapper.app.controller;

import com.wrapper.app.domain.Dan;
import com.wrapper.app.repository.DanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/dani")
public class DanController {

    private final DanRepository repository;

    public DanController(DanRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Dan> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dan getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }
}
