package com.wrapper.app.repository;

import com.wrapper.app.domain.Predmet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PredmetRepository extends MongoRepository<Predmet, String> {

    @Query("{$and:[{'studijskiProgram': ?0}, {'uRealizaciji': ?1}]}")
    List<Predmet> findByStudijskiProgram(String studijskiProgram, boolean uRealizaciji);

    @Query("{$and:[{'oznaka': {$regex : ?0, $options: 'i'}}, {'naziv': {$regex : ?1, $options: 'i'}}, {'studijskiProgram': ?2}]}")
    List<Predmet> search(String oznaka, String naziv, String studijskiProgram);
    
    Optional<Predmet> findByOznakaAndPlanAndStudijskiProgram(String oznaka, int plan, String studijskiProgram);
}
