package com.wrapper.app.application.service;

import com.wrapper.app.application.util.StudyTypes;
import com.wrapper.app.domain.model.StudijskiProgram;
import com.wrapper.app.presentation.dto.search.StudijskiProgramSearchDto;
import com.wrapper.app.domain.exception.AlreadyExistsException;
import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.infrastructure.persistence.repository.StudijskiProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudijskiProgramService {

    private final StudijskiProgramRepository repository;

    private static final String PRVI_STEPEN = "1";
    private static final String DRUGI_STEPEN = "2";
    private static final String AKADEMSKI_NIVO = "1";
    private static final String STRUKOVNE_OSNOVNI_NIVO = "2";
    private static final String MASTER_STRUKOVNE_NIVO = "5";

    public StudijskiProgramService(StudijskiProgramRepository repository) {
        this.repository = repository;
    }

    public List<StudijskiProgram> getAll() {
        return repository.findAll();
    }

    public List<StudijskiProgram> search(StudijskiProgramSearchDto searchDto) {
        return repository.search(searchDto.getOznaka(), searchDto.getNaziv(),
                getStepen(searchDto.getStepenStudija()), getNivo(searchDto.getStepenStudija()));
    }

    private String getStepen(String stepenStudija) {
        return switch (stepenStudija) {
            case StudyTypes.OAS, StudyTypes.OSS -> PRVI_STEPEN;
            case StudyTypes.MAS, StudyTypes.MSS -> DRUGI_STEPEN;
            default -> "";
        };
    }

    private String getNivo(String stepenStudija) {
        return switch (stepenStudija) {
            case StudyTypes.OAS, StudyTypes.MAS -> AKADEMSKI_NIVO;
            case StudyTypes.OSS -> STRUKOVNE_OSNOVNI_NIVO;
            case StudyTypes.MSS -> MASTER_STRUKOVNE_NIVO;
            default -> "";
        };
    }

    public StudijskiProgram getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
    }

    public StudijskiProgram add(StudijskiProgram studijskiProgram) {
        Optional<StudijskiProgram> existing = repository.findByOznakaAndNivoAndStepen(studijskiProgram.getOznaka(),
                studijskiProgram.getNivo(), studijskiProgram.getStepen());
        if(existing.isPresent())
            throw new AlreadyExistsException(StudijskiProgram.class.getSimpleName());
        studijskiProgram.setId(UUID.randomUUID().toString());
        return repository.save(studijskiProgram);
    }

    public StudijskiProgram update(String id, StudijskiProgram studijskiProgram) {
        if(!repository.existsById(id))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        Optional<StudijskiProgram> existing = repository.findByOznakaAndNivoAndStepen(studijskiProgram.getOznaka(),
                studijskiProgram.getNivo(), studijskiProgram.getStepen());
        if(existing.isPresent() && !existing.get().getId().equals(id))
            throw new AlreadyExistsException(StudijskiProgram.class.getSimpleName());
        studijskiProgram.setId(id);
        return repository.save(studijskiProgram);
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public List<StudijskiProgram> searchByNaziv(String searchParam) {
        return repository.searchByNaziv(searchParam.trim());
    }
}
