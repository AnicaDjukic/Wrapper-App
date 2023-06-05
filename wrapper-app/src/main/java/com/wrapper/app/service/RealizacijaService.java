package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.RealizacijaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealizacijaService {

    private final RealizacijaRepository repository;

    public RealizacijaService(RealizacijaRepository repository) {
        this.repository = repository;
    }

    public List<Realizacija> getAll() {
        return repository.findAll();
    }

    public Realizacija getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Realizacija.class.getSimpleName()));
    }

    public void addStudijskiProgram(StudijskiProgramPredmeti studijskiProgramPredmeti) {
        Realizacija realizacija = repository.findAll().get(0);
        realizacija.addStudijskiProgram(studijskiProgramPredmeti);
        repository.save(realizacija);
    }

    public void removeStudijskiProgram(String studijskiProgramid) {
        Realizacija realizacija = repository.findAll().get(0);
        realizacija.removeStudijskiProgram(studijskiProgramid);
        repository.save(realizacija);
    }
}
