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

    public PredmetService(PredmetRepository repository, StudijskiProgramService studijskiProgramService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
    }

    public Page<Predmet> getAll(Pageable pageable) {
        Page<Predmet> results = repository.findAll(pageable);
        return mapStudijskiProgram(results);
    }

    public Page<Predmet> search(PredmetSearchDto searchDto, Pageable pageable) {
        List<StudijskiProgram> studProgrami = studijskiProgramService.searchByNaziv(searchDto.getStudijskiProgram());
        List<String> studProgramIds = studProgrami.stream().map(StudijskiProgram::getId).toList();
        List<Predmet> results = new ArrayList<>();
        studProgramIds.forEach(id -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getNaziv(), id)));
        return mapStudijskiProgram(createPage(results, pageable));
    }

    private PageImpl<Predmet> createPage(List<Predmet> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<Predmet> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    private Page<Predmet> mapStudijskiProgram(Page<Predmet> list) {
        list.forEach(result -> {
            StudijskiProgram studijskiProgram = studijskiProgramService.getById(result.getStudijskiProgram());
            result.setStudijskiProgram(studijskiProgram.getOznaka() + " " + studijskiProgram.getNaziv());
        });
        return list;
    }

    public Predmet create(Predmet predmet) {
        if(!studijskiProgramService.existsById(predmet.getStudijskiProgram()))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        Optional<Predmet> existing = repository.findByOznakaAndPlanAndStudijskiProgram(predmet.getOznaka(), predmet.getPlan(), predmet.getStudijskiProgram());
        if(existing.isPresent()) {
            throw new AlreadyExistsException(Predmet.class.getSimpleName());
        }
        predmet.setId(UUID.randomUUID().toString());
        //realizacijaService.addPredmetPredavac(predmet)  // TODO: add to realizacija
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

    public Predmet update(String id, Predmet predmet) {
        if (!repository.existsById(id))
            throw new NotFoundException(Predmet.class.getSimpleName());
        if(!studijskiProgramService.existsById(predmet.getStudijskiProgram()))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        predmet.setId(id);
        return repository.save(predmet);
    }

    public List<Predmet> getByStudijskiProgram(String studijskiProgram) {
        return repository.findByStudijskiProgram(studijskiProgram);
    }

    public void deleteAllByStudijskiProgram(String studProgramId) {
        repository.deleteAllByStudijskiProgram(studProgramId);
    }
}
