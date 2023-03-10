package com.wrapper.app.controller;

import com.wrapper.app.domain.Katedra;
import com.wrapper.app.repository.KatedraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/katedre")
public class KatedraController {

    private final KatedraRepository repository;

    public KatedraController(KatedraRepository repository) {
        this.repository = repository;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Katedra> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Katedra getById(@PathVariable String id) {
        return repository.findById(id).orElseThrow(RuntimeException::new);
    }
}
