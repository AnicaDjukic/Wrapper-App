package com.wrapper.app.repository;

import com.wrapper.app.domain.StudijskiProgram;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudijskiProgramRepository extends MongoRepository<StudijskiProgram, String> {
}
