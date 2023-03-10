package com.wrapper.app.repository;

import com.wrapper.app.domain.Departman;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmanRepository extends MongoRepository<Departman, String> {
}
