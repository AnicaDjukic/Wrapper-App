package com.wrapper.app.repository;

import com.wrapper.app.domain.StudijskiProgram;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StudijskiProgramRepository extends MongoRepository<StudijskiProgram, String> {

    @Query("{'naziv': {$regex : ?0, $options: 'i'}}")
    List<StudijskiProgram> searchByNaziv(String naziv);
}
