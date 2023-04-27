package com.wrapper.app.repository;

import com.wrapper.app.domain.Departman;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DepartmanRepository extends MongoRepository<Departman, String> {

    @Query("{'naziv': {$regex : ?0, $options: 'i'}}")
    List<Departman> searchByNaziv(String naziv);
}
