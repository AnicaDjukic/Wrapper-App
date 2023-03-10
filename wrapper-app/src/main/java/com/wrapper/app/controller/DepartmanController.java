package com.wrapper.app.controller;

import com.wrapper.app.domain.Departman;
import com.wrapper.app.repository.DepartmanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/departmani")
public class DepartmanController {

    private final DepartmanRepository repository;

    public DepartmanController(DepartmanRepository repository) {
        this.repository = repository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Departman> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Departman getById(@PathVariable String id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }
}
