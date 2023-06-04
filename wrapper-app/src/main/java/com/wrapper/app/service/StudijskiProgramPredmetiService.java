package com.wrapper.app.service;

import com.wrapper.app.domain.PredmetPredavac;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudijskiProgramPredmetiRepository;
import org.springframework.stereotype.Service;

@Service
public class StudijskiProgramPredmetiService {

    private final StudijskiProgramPredmetiRepository repository;

    private final StudijskiProgramService studijskiProgramService;

    public StudijskiProgramPredmetiService(StudijskiProgramPredmetiRepository repository, StudijskiProgramService studijskiProgramService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
    }

    public StudijskiProgramPredmeti getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(StudijskiProgramPredmeti.class.getSimpleName()));
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
}
