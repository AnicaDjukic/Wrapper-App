package com.wrapper.app.repository;

import com.wrapper.app.domain.Predmet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PredmetRepository extends MongoRepository<Predmet, String> {

    boolean existsByOznaka(String oznaka);

    Optional<Predmet> findByOznaka(String oznaka);
}
