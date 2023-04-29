package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.StudentskaGrupaSearchDto;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudentskaGrupaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentskaGrupaService {

    private final StudentskaGrupaRepository repository;

    private final StudijskiProgramService studijskiProgramService;

    public StudentskaGrupaService(StudentskaGrupaRepository repository, StudijskiProgramService studijskiProgramService) {
        this.repository = repository;
        this.studijskiProgramService = studijskiProgramService;
    }

    public Page<StudentskaGrupa> getAll(Pageable pageable) {
        Page<StudentskaGrupa> results = repository.findAll(pageable);
        return mapStudProgram(results);
    }

    public Page<StudentskaGrupa> search(StudentskaGrupaSearchDto searchDto, Pageable pageable) {
        List<StudentskaGrupa> results = new ArrayList<>();
        List<String> studijskiProgramIds = studijskiProgramService.searchByNaziv(searchDto.getStudijskiProgram())
                .stream().map(StudijskiProgram::getId).toList();
        studijskiProgramIds.forEach(studProgId ->
                results.addAll(repository.search(searchDto.getOznaka(), searchDto.getGodina(), searchDto.getBrojStudenata(),studProgId)));
        Page<StudentskaGrupa> page = createPage(results, pageable);
        return mapStudProgram(page);
    }

    private Page<StudentskaGrupa> createPage(List<StudentskaGrupa> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<StudentskaGrupa> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    private Page<StudentskaGrupa> mapStudProgram(Page<StudentskaGrupa> list) {
        list.forEach(result -> {
            StudijskiProgram studijskiProgram = studijskiProgramService.getById(result.getStudijskiProgram());
            result.setStudijskiProgram(studijskiProgram.getOznaka() + " " + studijskiProgram.getNaziv());
        });
        return list;
    }

    public StudentskaGrupa getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(StudentskaGrupa.class.getSimpleName()));
    }

    public StudentskaGrupa create(StudentskaGrupa studentskaGrupa) {
        // TODO: do checks
        studentskaGrupa.setId(UUID.randomUUID().toString());
        return repository.save(studentskaGrupa);
    }

    public StudentskaGrupa update(String id, StudentskaGrupa studentskaGrupa) {
        // TODO: do checks
        if(!repository.existsById(id))
            throw new NotFoundException(StudentskaGrupa.class.getSimpleName());
        studentskaGrupa.setId(id);
        return repository.save(studentskaGrupa);
    }

    public StudentskaGrupa deleteById(String id) {
        StudentskaGrupa studentskaGrupa = getById(id);
        repository.delete(studentskaGrupa);
        return studentskaGrupa;
    }
}
