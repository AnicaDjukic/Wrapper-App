package com.wrapper.app.repository;

import com.wrapper.app.domain.Predavac;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PredavacRepository extends MongoRepository<Predavac, String> {
}
