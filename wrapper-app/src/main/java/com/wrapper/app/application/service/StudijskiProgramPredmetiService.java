package com.wrapper.app.application.service;

import com.wrapper.app.domain.model.PredmetPredavac;
import com.wrapper.app.domain.model.StudijskiProgram;
import com.wrapper.app.domain.model.StudijskiProgramPredmeti;
import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.infrastructure.persistence.repository.StudijskiProgramPredmetiRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudijskiProgramPredmetiService {

    private final StudijskiProgramPredmetiRepository repository;

    private final StudijskiProgramService studijskiProgramService;

    public StudijskiProgramPredmetiService(StudijskiProgramPredmetiRepository repository,
                                           StudijskiProgramService studijskiProgramService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
    }

    public StudijskiProgramPredmeti getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StudijskiProgramPredmeti.class.getSimpleName()));
    }

    public StudijskiProgramPredmeti add(StudijskiProgram studijskiProgram) {
        StudijskiProgram savedStudijskiProgram = studijskiProgramService.add(studijskiProgram);
        StudijskiProgramPredmeti studijskiProgramPredmeti = new StudijskiProgramPredmeti();
        studijskiProgramPredmeti.setId(savedStudijskiProgram.getId());
        studijskiProgramPredmeti.setStudijskiProgram(savedStudijskiProgram);
        studijskiProgramPredmeti.setPredmetPredavaci(new ArrayList<>());
        return repository.save(studijskiProgramPredmeti);
    }

    public StudijskiProgramPredmeti addPredmet(String studProgramId, PredmetPredavac predmetPredavac) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studProgramId);
        studijskiProgramPredmeti.addPredmetPredavac(predmetPredavac);
        return repository.save(studijskiProgramPredmeti);
    }

    public StudijskiProgramPredmeti updatePredmet(String studProgramId, String predmetId, PredmetPredavac predmetPredavac) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studProgramId);
        studijskiProgramPredmeti.updatePredmetPredavac(predmetId, predmetPredavac);
        return repository.save(studijskiProgramPredmeti);
    }

    public void removePredmet(String studProgramId, String predmetId) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studProgramId);
        studijskiProgramPredmeti.removePredmet(predmetId);
        repository.save(studijskiProgramPredmeti);
        studijskiProgramPredmeti.removePredmetPredavac(predmetId);
        repository.save(studijskiProgramPredmeti);
    }

    public void removePredavac(String predavacId) {
        for (StudijskiProgramPredmeti studijskiProgramPredmeti : repository.findByPredavacId(predavacId)) {
            studijskiProgramPredmeti.removePredavac(predavacId);
            repository.save(studijskiProgramPredmeti);
        }
    }

    public void updateBlockStatus(String studProgramId) {
        StudijskiProgramPredmeti studijskiProgramPredmeti = getById(studProgramId);
        studijskiProgramPredmeti.updateBlockStatus();
        repository.save(studijskiProgramPredmeti);
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public List<StudijskiProgramPredmeti> getAll() {
        return repository.findAll();
    }
}
