package com.wrapper.app.service;

import com.wrapper.app.domain.Realizacija;
import com.wrapper.app.dto.RealizacijaRequestDto;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.RealizacijaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealizacijaService {

    private final RealizacijaRepository repository;

    private final PredavacService predavacService;

    public RealizacijaService(RealizacijaRepository repository, PredavacService predavacService) {
        this.repository = repository;
        this.predavacService = predavacService;
    }

    public List<Realizacija> getAll() {
        return repository.findAll();
    }

    public Realizacija getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Realizacija.class.getSimpleName()));
    }

    // rukovanje predavacima na predmetima
    public Realizacija update(String id, RealizacijaRequestDto dto) {
        Realizacija realizacija = getById(id);
        check(dto);
        // TODO: provera da li ukupan broj termina vezbi odgovara broju termina vezbi na predmetu
        Realizacija updated = realizacija.update(dto);
        return repository.save(updated);
    }

    // provera da li predavaci postoje
    private void check(RealizacijaRequestDto dto) {
        predavacService.getById(dto.getProfesorId());
        dto.getOstaliProfesori().forEach(predavacService::getById);
        dto.getAsistentZauzeca().forEach(az -> predavacService.getById(az.getAsistentId()));
    }
}
