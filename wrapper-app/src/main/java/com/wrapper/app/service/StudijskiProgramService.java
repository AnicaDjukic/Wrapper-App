package com.wrapper.app.service;

import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.dto.StudijskiProgramSearchDto;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.StudijskiProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudijskiProgramService {

    private final String OAS = "OSNOVNE AKADEMSKE STUDIJE";
    private final String OSS = "OSNOVNE STRUKOVNE STUDIJE";
    private final String MAS = "MASTER AKADEMSKE STUDIJE";
    private final String MSS = "MASTER STRUKOVNE STUDIJE";

    private final StudijskiProgramRepository repository;

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
            case OAS, OSS -> "1";
            case MAS, MSS -> "2";
            default -> "";
        };
    }

    private String getNivo(String stepenStudija) {
        return switch (stepenStudija) {
            case OAS, MAS -> "1";
            case OSS -> "2";
            case MSS -> "5";
            default -> "";
        };
    }

    public StudijskiProgram getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
    }

    public StudijskiProgram create(StudijskiProgram studijskiProgram) {
        Optional<StudijskiProgram> existing = repository.findByOznaka(studijskiProgram.getOznaka());
        if(existing.isPresent())
            throw new AlreadyExistsException(StudijskiProgram.class.getSimpleName());
        studijskiProgram.setId(UUID.randomUUID().toString());
        return repository.save(studijskiProgram);
    }

    public StudijskiProgram update(String id, StudijskiProgram studijskiProgram) {
        if(!repository.existsById(id))
            throw new NotFoundException(StudijskiProgram.class.getSimpleName());
        Optional<StudijskiProgram> existing = repository.findByOznaka(studijskiProgram.getOznaka());
        if(existing.isPresent() && !existing.get().getId().equals(id))
            throw new AlreadyExistsException(StudijskiProgram.class.getSimpleName());
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

    public List<StudijskiProgram> searchByNaziv(String searchParam) {
        return repository.searchByNaziv(searchParam.trim());
    }

    public String getStepenStudija(int stepen, int nivo) {
        return switch (stepen) {
            case 1 -> (nivo == 1) ? OAS : OSS;
            case 2 -> (nivo == 1) ? MAS : MSS;
            default -> "";
        };
    }

    public void updateStatus(String id, boolean block) {
        StudijskiProgram studijskiProgram = getById(id);
        studijskiProgram.setBlock(block);
        repository.save(studijskiProgram);
    }
}
