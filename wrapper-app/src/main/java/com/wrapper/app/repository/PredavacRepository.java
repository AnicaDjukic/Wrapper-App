package com.wrapper.app.repository;

import com.wrapper.app.domain.Predavac;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PredavacRepository extends MongoRepository<Predavac, String> {

    @Query("{ $and: [ { $expr: { $regexMatch: { input: { $toString: '$oznaka' }, regex: ?0, options: 'i' } } }," +
            " { 'ime': { $regex: ?1, $options: 'i' } }, { 'prezime': { $regex: ?2, $options: 'i' } }," +
            " { 'orgJedinica': ?3 } ] }")
    List<Predavac> search(String oznaka, String ime, String prezime, String orgJedinica);

    @Query("{ $and: [ { $expr: { $regexMatch: { input: { $toString: '$oznaka' }, regex: ?0, options: 'i' } } }," +
            " { 'ime': { $regex: ?1, $options: 'i' } }, { 'prezime': { $regex: ?2, $options: 'i' } } ] }")
    List<Predavac> search(String oznaka, String ime, String prezime);

    Optional<Predavac> findByOznaka(int oznaka);
}
