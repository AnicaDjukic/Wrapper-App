package com.wrapper.app.repository;

import com.wrapper.app.domain.Prostorija;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProstorijaRepository extends MongoRepository<Prostorija, String> {

    @Query("{$and:[{'oznaka': {$regex : ?0, $options: 'i'}}, {'tip': {$regex : ?1, $options: 'i'}}, { $expr: { $regexMatch: { input: { $toString: '$kapacitet' }, regex: { $cond: [ { $eq: [ '?2', '' ] }, '.*', '^?2$' ] }, options: 'i' } } } { 'orgJedinica': { $in: [ ?3 ] }}]}")
    List<Prostorija> search(String oznaka, String tip, String kapacitet, String orgJedId);

    @Query("{$and:[{'oznaka': {$regex : ?0, $options: 'i'}}, {'tip': {$regex : ?1, $options: 'i'}}, { $expr: { $regexMatch: { input: { $toString: '$kapacitet' }, regex: { $cond: [ { $eq: [ '?2', '' ] }, '.*', '^?2$' ] }, options: 'i' } } }]}")
    List<Prostorija> searchWithoutOrgJedinica(String oznaka, String tip, String kapacitet);

    Optional<Prostorija> findByOznaka(String oznaka);
}
