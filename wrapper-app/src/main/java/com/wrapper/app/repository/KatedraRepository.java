package com.wrapper.app.repository;

import com.wrapper.app.domain.Katedra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface KatedraRepository extends MongoRepository<Katedra, String> {

    @Query("{'naziv': {$regex : ?0, $options: 'i'}}")
    List<Katedra> searchByNaziv(String naziv);
}
