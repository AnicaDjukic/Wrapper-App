package com.wrapper.app.application.service;

import com.wrapper.app.presentation.dto.search.StudentskaGrupaSearchDto;
import com.wrapper.app.domain.model.StudentskaGrupa;
import com.wrapper.app.domain.model.StudijskiProgram;
import com.wrapper.app.presentation.dto.request.StudentskeGrupeRequestDto;
import com.wrapper.app.domain.exception.AlreadyExistsException;
import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.infrastructure.persistence.repository.StudentskaGrupaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentskaGrupaService {

    private final StudentskaGrupaRepository repository;

    private final StudijskiProgramService studijskiProgramService;

    public StudentskaGrupaService(StudentskaGrupaRepository repository, StudijskiProgramService studijskiProgramService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
    }

    public Page<StudentskaGrupa> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<StudentskaGrupa> search(StudentskaGrupaSearchDto searchDto, Pageable pageable) {
        List<StudentskaGrupa> results = new ArrayList<>();
        List<String> studijskiProgramIds = studijskiProgramService.searchByNaziv(searchDto.getStudijskiProgram())
                .stream().map(StudijskiProgram::getId).toList();
        studijskiProgramIds.forEach(studProgId -> results.addAll(repository.search(searchDto.getOznaka(),
                searchDto.getGodina(), searchDto.getBrojStudenata(), studProgId)));
        return createPage(results, pageable);
    }

    private Page<StudentskaGrupa> createPage(List<StudentskaGrupa> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<StudentskaGrupa> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    public StudentskaGrupa getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StudentskaGrupa.class.getSimpleName()));
    }

    public List<StudentskaGrupa> create(StudentskeGrupeRequestDto studentskeGrupe) {
        if(!studijskiProgramService.existsById(studentskeGrupe.getStudijskiProgramId()))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        int maxOznaka = getMaxOznaka(studentskeGrupe);
        return generateNewStudentskeGrupe(studentskeGrupe, maxOznaka);
    }

    private int getMaxOznaka(StudentskeGrupeRequestDto studentskeGrupe) {
        List<StudentskaGrupa> existing = repository.findAllByGodinaAndSemestarAndStudijskiProgram(
                studentskeGrupe.getGodina(),
                studentskeGrupe.getSemestar(),
                studentskeGrupe.getStudijskiProgramId());
        Optional<StudentskaGrupa> grupaWithMaxOznaka = existing.stream()
                .max(Comparator.comparing(StudentskaGrupa::getOznaka));
        return grupaWithMaxOznaka.map(studentskaGrupa -> studentskaGrupa.getOznaka() + 1).orElse(1);
    }

    private List<StudentskaGrupa> generateNewStudentskeGrupe(StudentskeGrupeRequestDto studentskeGrupe, int maxOznaka) {
        List<StudentskaGrupa> results = new ArrayList<>();
        for(int i = 0; i < studentskeGrupe.getBrojStudentskihGrupa(); i++) {
            StudentskaGrupa studentskaGrupa = new StudentskaGrupa(
                    UUID.randomUUID().toString(),
                    maxOznaka + i,
                    studentskeGrupe.getGodina(),
                    studentskeGrupe.getSemestar(),
                    studentskeGrupe.getBrojStudenata(),
                    studijskiProgramService.getById(studentskeGrupe.getStudijskiProgramId()));
            StudentskaGrupa saved = repository.save(studentskaGrupa);
            results.add(saved);
        }
        return results;
    }

    public StudentskaGrupa update(String id, StudentskaGrupa studentskaGrupa) {
        Optional<StudentskaGrupa> existing = repository.findByOznakaAndGodinaAndSemestarAndStudijskiProgram(
                studentskaGrupa.getOznaka(), studentskaGrupa.getGodina(),
                studentskaGrupa.getSemestar(), studentskaGrupa.getStudijskiProgram().getId());
        if(existing.isPresent() && !existing.get().getId().equals(id))
            throw new AlreadyExistsException(StudentskaGrupa.class.getSimpleName());
        studentskaGrupa.setId(id);
        return repository.save(studentskaGrupa);
    }

    public StudentskaGrupa deleteById(String id) {
        StudentskaGrupa studentskaGrupa = getById(id);
        repository.delete(studentskaGrupa);
        return studentskaGrupa;
    }
}
