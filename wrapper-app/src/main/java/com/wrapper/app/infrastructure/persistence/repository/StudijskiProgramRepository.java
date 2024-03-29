package com.wrapper.app.infrastructure.persistence.repository;

import com.wrapper.app.domain.model.StudijskiProgram;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StudijskiProgramRepository extends MongoRepository<StudijskiProgram, String> {

    @Query("{'naziv': {$regex : ?0, $options: 'i'}}")
    List<StudijskiProgram> searchByNaziv(String naziv);

    @Query("{$and:[{'oznaka': {$regex : ?0, $options: 'i'}}, {'naziv': {$regex : ?1, $options: 'i'}}," +
            "{ $expr: { $regexMatch: { input: { $toString: '$stepen' }, regex: { $cond: [ { $eq: [ '?2', '' ] }, '.*', '^?2$' ] }, options: 'i' } } }," +
            "{ $expr: { $regexMatch: { input: { $toString: '$nivo' }, regex: { $cond: [ { $eq: [ '?3', '' ] }, '.*', '^?3$' ] }, options: 'i' } } } ]}")
    List<StudijskiProgram> search(String oznaka, String naziv, String stepen, String nivo);

    Optional<StudijskiProgram> findByOznakaAndNivoAndStepen(String oznaka, int nivo, int stepen);
}
