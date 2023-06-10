package com.wrapper.app.repository;

import com.wrapper.app.domain.Realizacija;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RealizacijaRepository extends MongoRepository<Realizacija, String> {

}
