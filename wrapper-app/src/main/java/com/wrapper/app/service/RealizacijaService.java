package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.RealizacijaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealizacijaService {

    private final RealizacijaRepository repository;

    private final PredavacService predavacService;

    private final StudijskiProgramService studijskiProgramService;

    private final PredmetService predmetService;

    public RealizacijaService(RealizacijaRepository repository, PredavacService predavacService,
                              StudijskiProgramService studijskiProgramService, PredmetService predmetService) {
        this.repository = repository;
        this.predavacService = predavacService;
        this.studijskiProgramService = studijskiProgramService;
        this.predmetService = predmetService;
    }

    public List<Realizacija> getAll() {
        return repository.findAll();
    }

    public Realizacija getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Realizacija.class.getSimpleName()));
    }

    public Realizacija addPredmet(String studijskiProgramId, PredmetPredavac predmetPredavac) {
        validatePredmet(predmetPredavac);
        validateStudijskiProgram(studijskiProgramId, predmetPredavac);
        validatePredavaci(predmetPredavac);
        Realizacija updated = addPredmetToRealizacija(studijskiProgramId, predmetPredavac);
        return repository.save(updated);
    }

    public Realizacija updatePredmet(String studProgramId, String predmetId, PredmetPredavac predmetPredavac) {
        validatePredmet(predmetPredavac);
        validateStudijskiProgram(studProgramId, predmetPredavac);
        validatePredavaci(predmetPredavac);
        Realizacija realizacija = getAll().get(0);
        Realizacija updated = realizacija.updatePredmet(studProgramId, predmetId, predmetPredavac);
        return repository.save(updated);
    }

    private Realizacija addPredmetToRealizacija(String studijskiProgramId, PredmetPredavac predmetPredavac) {
        Realizacija realizacija = getAll().get(0);
        Realizacija updated = realizacija.addPredmet(studijskiProgramId, predmetPredavac);
        predmetService.updateStatus(predmetPredavac.getPredmetId(), true);
        return updated;
    }

    private void validatePredmet(PredmetPredavac predmetPredavac) {
        Predmet predmet = predmetService.getById(predmetPredavac.getPredmetId());
        boolean checkAsistentZauzeca = predmetPredavac.getAsistentZauzeca().isEmpty() &&
                (predmet.getBrojCasovaAud() != 0 || predmet.getBrojCasovaLab() != 0
                        || predmet.getBrojCasovaRac() != 0);
        boolean block = predmetPredavac.getProfesorId() == null || checkAsistentZauzeca;
        predmetPredavac.setBlock(block);
    }

    private void validateStudijskiProgram(String studProgramId, PredmetPredavac predmetPredavac) {
        boolean block = predmetPredavac.isBlock();
        if(!block) {
            Realizacija realizacija = repository.findStudijskiProgramPredmetiByStudijskiProgramId(studProgramId);
            StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().get(0);
            for(PredmetPredavac predmet : studijskiProgramPredmeti.getPredmetPredavaci()) {
                if(predmet.isBlock() && !predmet.getPredmetId().equals(predmetPredavac.getPredmetId())) {
                    block = true;
                    break;
                }
            }
        }
        studijskiProgramService.updateStatus(studProgramId, block);
    }

    private void validatePredavaci(PredmetPredavac predmetPredavac) {
        if(predmetPredavac.getProfesorId() != null) {
            predavacService.getById(predmetPredavac.getProfesorId());
        }
        predmetPredavac.getOstaliProfesori().forEach(predavacService::getById);
        predmetPredavac.getAsistentZauzeca().forEach(az -> predavacService.getById(az.getAsistentId()));
    }

    public StudijskiProgramPredmeti getStudijskiProgramById(String studijskiProgramId) {
        Realizacija realizacija = repository.findStudijskiProgramPredmetiByStudijskiProgramId(studijskiProgramId);
        return realizacija.getStudijskiProgramPredmeti().get(0);
    }

    public void deletePredmetInStudijskiProgram(String studijskiProgramId, String predmetId) {
        Realizacija realizacija = repository.findAll().get(0);
        StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().stream()
                .filter(p -> p.getStudijskiProgramId().equals(studijskiProgramId))
                .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
        PredmetPredavac predmetPredavac = studijskiProgramPredmeti.getPredmetPredavaci().stream()
                        .filter(p -> p.getPredmetId().equals(predmetId))
                        .findFirst().orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
        studijskiProgramPredmeti.getPredmetPredavaci().remove(predmetPredavac);
        repository.save(realizacija);
    }

    public void removePredmet(String predmetId) {
        Realizacija realizacija = repository.findAll().get(0);
        repository.save(realizacija.removePredmet(predmetId));
        predmetService.deleteById(predmetId);
    }

    public void removePredavac(String predavacId) {
        Realizacija realizacija = repository.findAll().get(0);
        repository.save(realizacija.removePredavac(predavacId));
        predavacService.deleteById(predavacId);
    }

    public void removeStudijskiProgram(String studProgramId) {
        Realizacija realizacija = repository.findAll().get(0);
        repository.save(realizacija.removeStudijskiProgram(studProgramId));
        studijskiProgramService.deleteById(studProgramId);
        predmetService.deleteAllByStudijskiProgram(studProgramId);
    }
}
