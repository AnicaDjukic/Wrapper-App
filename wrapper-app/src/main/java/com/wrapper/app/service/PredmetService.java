package com.wrapper.app.service;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.StudijskiProgram;
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

    public PredmetService(PredmetRepository repository,
                          StudijskiProgramService studijskiProgramService) {
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

    public Predmet add(Predmet predmet) {
        Optional<Predmet> existing = repository.findByOznakaAndPlanAndStudijskiProgram(predmet.getOznaka(), predmet.getPlan(), predmet.getStudijskiProgram().getId());
        if(existing.isPresent()) {
            throw new AlreadyExistsException(Predmet.class.getSimpleName());
        }
        predmet.setId(UUID.randomUUID().toString());
        return repository.save(predmet);
    }

    public Predmet getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Predmet update(String id, Predmet predmet) {
        Optional<Predmet> existing = repository.findByOznakaAndPlanAndStudijskiProgram(predmet.getOznaka(),
                predmet.getPlan(), predmet.getStudijskiProgram().getId());
        if(existing.isPresent() && !existing.get().getId().equals(id)) {
            throw new AlreadyExistsException(Predmet.class.getSimpleName());
        }
        Predmet old = getById(id);
        predmet.setId(id);
        predmet.setURealizaciji(old.getURealizaciji());
        return repository.save(predmet);
    }

    public List<Predmet> getByStudijskiProgram(String studijskiProgram, boolean uRealizaciji) {
        return repository.findByStudijskiProgram(studijskiProgram, uRealizaciji);
    }

    public void deleteAllByStudijskiProgram(String studProgramId) {
        repository.deleteAllByStudijskiProgram(studProgramId);
    }

    public void updateRealizacijaStatus(String predmetId, boolean uRealizaciji) {
        Predmet predmet = getById(predmetId);
        predmet.setURealizaciji(uRealizaciji);
        repository.save(predmet);
    }
}
