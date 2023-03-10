package com.wrapper.app.repository;

import com.wrapper.app.domain.Katedra;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KatedraRepository extends MongoRepository<Katedra, String> {
}
