package com.wrapper.app.repository;

import com.wrapper.app.domain.Predmet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PredmetRepository extends MongoRepository<Predmet, String> {

    List<Predmet> findByStudijskiProgram(String studijskiProgram);
}
