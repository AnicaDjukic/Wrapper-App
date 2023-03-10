package com.wrapper.app.service;

import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudijskiProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StudijskiProgramService {

    private final StudijskiProgramRepository repository;

    public StudijskiProgramService(StudijskiProgramRepository repository) {
        this.repository = repository;
    }

    public List<StudijskiProgram> getAll() {
        return repository.findAll();
    }

    public StudijskiProgram getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
    }

    public StudijskiProgram create(StudijskiProgram studijskiProgram) {
        studijskiProgram.setId(UUID.randomUUID().toString());
        return repository.save(studijskiProgram);
    }

    public StudijskiProgram update(String id, StudijskiProgram studijskiProgram) {
        if(!repository.existsById(id))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        studijskiProgram.setId(id);
        return repository.save(studijskiProgram);
    }

    public StudijskiProgram deleteById(String id) {
        StudijskiProgram studijskiProgram = getById(id);
        repository.delete(studijskiProgram);
        return studijskiProgram;
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}
