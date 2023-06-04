package com.wrapper.app.repository;

import com.wrapper.app.domain.StudijskiProgramPredmeti;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudijskiProgramPredmetiRepository extends MongoRepository<StudijskiProgramPredmeti, String> {
}
