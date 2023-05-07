package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.StudentskaGrupaSearchDto;
import com.wrapper.app.dto.StudentskeGrupeRequestDto;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudentskaGrupaRepository;
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

    public List<StudentskaGrupa> create(StudentskeGrupeRequestDto studentskeGrupe) {
        if(!studijskiProgramService.existsById(studentskeGrupe.getStudijskiProgram()))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        int maxOznaka = getMaxOznaka(studentskeGrupe);
        return generateNewStudentskeGrupe(studentskeGrupe, maxOznaka);
    }

    private int getMaxOznaka(StudentskeGrupeRequestDto studentskeGrupe) {
        List<StudentskaGrupa> existing = repository.findAllByGodinaAndSemestarAndStudijskiProgram(studentskeGrupe.getGodina(),
                studentskeGrupe.getSemestar(),
                studentskeGrupe.getStudijskiProgram());
        Optional<StudentskaGrupa> grupaWithMaxOznaka = existing.stream().max(Comparator.comparing(StudentskaGrupa::getOznaka));
        int maxOznaka = 1;
        if(grupaWithMaxOznaka.isPresent()) {
            maxOznaka = grupaWithMaxOznaka.get().getOznaka() + 1;
        }
        return maxOznaka;
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
                    studentskeGrupe.getStudijskiProgram());
            StudentskaGrupa saved = repository.save(studentskaGrupa);
            results.add(saved);
        }
        return results;
    }

    public StudentskaGrupa update(String id, StudentskaGrupa studentskaGrupa) {
        if(!repository.existsById(id))
            throw new NotFoundException(StudentskaGrupa.class.getSimpleName());
        Optional<StudentskaGrupa> existing = repository.findByOznakaAndGodinaAndSemestarAndStudijskiProgram(studentskaGrupa.getOznaka(),
                studentskaGrupa.getGodina(), studentskaGrupa.getSemestar(), studentskaGrupa.getStudijskiProgram());
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
