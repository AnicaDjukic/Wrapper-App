package com.wrapper.app.repository;

import com.wrapper.app.domain.Database;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DatabaseRepository extends MongoRepository<Database, String> {

    Optional<Database> findBySemestarAndGodina(String semestar, String godina);

    Optional<Database> findByGenerationFinishedIsNullAndGenerationStartedIsNotNull();
}
