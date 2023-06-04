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

//
//    public void deletePredmetInStudijskiProgram(String studijskiProgramId, String predmetId) {
//        Realizacija realizacija = repository.findAll().get(0);
//        StudijskiProgramPredmeti studijskiProgramPredmeti = realizacija.getStudijskiProgramPredmeti().stream()
//                .filter(p -> p.getStudijskiProgram().equals(studijskiProgramId))
//                .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
//        PredmetPredavac predmetPredavac = studijskiProgramPredmeti.getPredmetPredavaci().stream()
//                        .filter(p -> p.getPredmet().equals(predmetId))
//                        .findFirst().orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
//        studijskiProgramPredmeti.getPredmetPredavaci().remove(predmetPredavac);
//        repository.save(realizacija);
//    }
//
//    public void removePredmet(String predmetId) {
//        Realizacija realizacija = repository.findAll().get(0);
//        repository.save(realizacija.removePredmet(predmetId));
//        predmetService.deleteById(predmetId);
//    }
//
//    public void removePredavac(String predavacId) {
//        Realizacija realizacija = repository.findAll().get(0);
//        repository.save(realizacija.removePredavac(predavacId));
//        predavacService.deleteById(predavacId);
//    }
//
//    public void removeStudijskiProgram(String studProgramId) {
//        Realizacija realizacija = repository.findAll().get(0);
//        repository.save(realizacija.removeStudijskiProgram(studProgramId));
//        studijskiProgramService.deleteById(studProgramId);
//        predmetService.deleteAllByStudijskiProgram(studProgramId);
//    }
}
