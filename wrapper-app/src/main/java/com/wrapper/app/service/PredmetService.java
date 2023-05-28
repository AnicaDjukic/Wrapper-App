package com.wrapper.app.service;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.dto.PredmetRequestDto;
import com.wrapper.app.dto.PredmetSearchDto;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.PredmetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PredmetService {

    private final PredmetRepository repository;

    private final StudijskiProgramService studijskiProgramService;

    public PredmetService(PredmetRepository repository, StudijskiProgramService studijskiProgramService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
    }

    public List<Predmet> findAll() {
        return repository.findAll();
    }

    public Page<Predmet> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Predmet> search(PredmetSearchDto searchDto, Pageable pageable) {
        List<StudijskiProgram> studProgrami = studijskiProgramService.searchByNaziv(searchDto.getStudijskiProgram());
        List<String> studProgramIds = studProgrami.stream().map(StudijskiProgram::getId).toList();
        List<Predmet> results = new ArrayList<>();
        studProgramIds.forEach(id -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getNaziv(), id)));
        return createPage(results, pageable);
    }

    private PageImpl<Predmet> createPage(List<Predmet> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<Predmet> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    public Predmet create(PredmetRequestDto dto) {
        Optional<Predmet> existing = repository.findByOznakaAndPlanAndStudijskiProgram(dto.getOznaka(), dto.getPlan(), dto.getStudijskiProgram());
        if(existing.isPresent()) {
            throw new AlreadyExistsException(Predmet.class.getSimpleName());
        }
        Predmet predmet = createPredmet(dto);
        predmet.setId(UUID.randomUUID().toString());
        return repository.save(predmet);
    }

    public Predmet getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
    }

    public Predmet deleteById(String id) {
        Predmet predmet = getById(id);
        repository.delete(predmet);
        return predmet;
    }

    public Predmet update(String id, PredmetRequestDto dto) {
        Optional<Predmet> existing = repository.findByOznakaAndPlanAndStudijskiProgram(dto.getOznaka(), dto.getPlan(), dto.getStudijskiProgram());
        if(existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new AlreadyExistsException(Predmet.class.getSimpleName());
        }
        Predmet predmet = createPredmet(dto);
        predmet.setId(id);
        return repository.save(predmet);
    }

    private Predmet createPredmet(PredmetRequestDto dto) {
        StudijskiProgram studijskiProgram = studijskiProgramService.getById(dto.getStudijskiProgram());
        Predmet predmet = Predmet.builder().oznaka(dto.getOznaka())
                .plan(dto.getPlan()).naziv(dto.getNaziv()).godina(dto.getGodina()).sifraStruke(dto.getSifraStruke())
                .brojCasovaPred(dto.getBrojCasovaPred()).brojCasovaAud(dto.getBrojCasovaAud())
                .brojCasovaLab(dto.getBrojCasovaLab()).brojCasovaRac(dto.getBrojCasovaRac()).build();
        predmet.setStudijskiProgram(studijskiProgram);
        return predmet;
    }

    public List<Predmet> getByStudijskiProgram(String studijskiProgram, boolean uRealizaciji) {
        return repository.findByStudijskiProgram(studijskiProgram, uRealizaciji);
    }

    public void deleteAllByStudijskiProgram(String studProgramId) {
        repository.deleteAllByStudijskiProgram(studProgramId);
    }

    public void updateStatus(String predmetId, boolean uRealizaciji) {
        Optional<Predmet> predmet = repository.findById(predmetId);
        if(predmet.isPresent()) {
            predmet.get().setURealizaciji(uRealizaciji);
            repository.save(predmet.get());
        }
    }
}
