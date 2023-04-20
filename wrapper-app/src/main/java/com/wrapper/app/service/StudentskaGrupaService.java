package com.wrapper.app.service;

import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudentskaGrupaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        results.forEach(result -> {
            StudijskiProgram studijskiProgram = studijskiProgramService.getById(result.getStudijskiProgram());
            result.setStudijskiProgram(studijskiProgram.getOznaka() + " " + studijskiProgram.getNaziv());
        });
        return results;
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
