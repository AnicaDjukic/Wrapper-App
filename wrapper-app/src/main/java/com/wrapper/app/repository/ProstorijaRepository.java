package com.wrapper.app.repository;

import com.wrapper.app.domain.Prostorija;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProstorijaRepository extends MongoRepository<Prostorija, String> {
}
