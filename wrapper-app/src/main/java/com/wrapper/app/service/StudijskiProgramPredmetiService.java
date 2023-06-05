package com.wrapper.app.service;

import com.wrapper.app.domain.PredmetPredavac;
import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudijskiProgramPredmetiRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class StudijskiProgramPredmetiService {

    private final StudijskiProgramPredmetiRepository repository;

    private final StudijskiProgramService studijskiProgramService;

    private final RealizacijaService realizacijaService;

    public StudijskiProgramPredmetiService(StudijskiProgramPredmetiRepository repository, StudijskiProgramService studijskiProgramService, RealizacijaService realizacijaService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
        this.realizacijaService = realizacijaService;
    }

    public StudijskiProgramPredmeti getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(StudijskiProgramPredmeti.class.getSimpleName()));
    }

    public StudijskiProgramPredmeti create(StudijskiProgram studijskiProgram) {
        StudijskiProgram savedStudijskiProgram = studijskiProgramService.create(studijskiProgram);
        StudijskiProgramPredmeti studijskiProgramPredmeti = new StudijskiProgramPredmeti();
        studijskiProgramPredmeti.setId(savedStudijskiProgram.getId());
        studijskiProgramPredmeti.setStudijskiProgram(savedStudijskiProgram);
        studijskiProgramPredmeti.setPredmetPredavaci(new ArrayList<>());
        StudijskiProgramPredmeti saved = repository.save(studijskiProgramPredmeti);
        realizacijaService.addStudijskiProgram(saved);
        return saved;
    }

    public StudijskiProgramPredmeti addPredmet(String studProgramId, PredmetPredavac predmetPredavac) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studProgramId);
        studijskiProgramPredmeti.addPredmet(predmetPredavac);
        studijskiProgramService.update(studijskiProgramPredmeti.getStudijskiProgram().getId(), studijskiProgramPredmeti.getStudijskiProgram());
        return repository.save(studijskiProgramPredmeti);
    }

    public StudijskiProgramPredmeti updatePredmet(String studProgramId, String predmetId, PredmetPredavac predmetPredavac) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studProgramId);
        studijskiProgramPredmeti.updatePredmet(predmetId, predmetPredavac);
        studijskiProgramService.update(studijskiProgramPredmeti.getStudijskiProgram().getId(), studijskiProgramPredmeti.getStudijskiProgram());
        return repository.save(studijskiProgramPredmeti);
    }

    public void removePredmet(String studijskiProgramId, String predmetId) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studijskiProgramId);
        studijskiProgramPredmeti.removePredmet(predmetId);
        studijskiProgramService.update(studijskiProgramPredmeti.getStudijskiProgram().getId(), studijskiProgramPredmeti.getStudijskiProgram());
        repository.save(studijskiProgramPredmeti);
    }

    public void deleteById(String id) {
        realizacijaService.removeStudijskiProgram(id);
        studijskiProgramService.deleteById(id);
        repository.deleteById(id);
    }

}
