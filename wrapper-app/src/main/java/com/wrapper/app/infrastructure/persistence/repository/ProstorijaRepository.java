package com.wrapper.app.infrastructure.persistence.repository;

import com.wrapper.app.domain.model.Prostorija;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProstorijaRepository extends MongoRepository<Prostorija, String> {

    @Query("{$and:[{'oznaka': {$regex : ?0, $options: 'i'}}, {'tip': {$regex : ?1, $options: 'i'}}, " +
            " { $or: [ { $expr: { $eq: ['?2', ''] } }, { 'sekundarniTip': { $regex: ?2, $options: 'i' } } ] }," +
            " { $expr: { $regexMatch: { input: { $toString: '$kapacitet' }," +
            " regex: { $cond: [ { $eq: [ '?3', '' ] }, '.*', '^?3$' ] }, options: 'i' } } }," +
            " { $or: [ { 'orgJedinica': { $in: [ ?4 ] } }, { 'sekundarnaOrgJedinica': { $in: [ ?4 ] } } ] }]}")
    List<Prostorija> search(String oznaka, String tip, String sekundarniTip, String kapacitet, String orgJedIds);

    @Query("{$and:[{'oznaka': {$regex : ?0, $options: 'i'}}, {'tip': {$regex : ?1, $options: 'i'}}, " +
            " { $or: [ { $expr: { $eq: ['?2', ''] } }, { 'sekundarniTip': { $regex: ?2, $options: 'i' } } ] }," +
            " { $expr: { $regexMatch: { input: { $toString: '$kapacitet' }," +
            " regex: { $cond: [ { $eq: [ '?3', '' ] }, '.*', '^?3$' ] }, options: 'i' } } }]}")
    List<Prostorija> search(String oznaka, String tip, String sekundarniTip, String kapacitet);

    Optional<Prostorija> findByOznaka(String oznaka);
}
