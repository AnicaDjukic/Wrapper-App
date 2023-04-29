package com.wrapper.app.repository;

import com.wrapper.app.domain.StudentskaGrupa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StudentskaGrupaRepository extends MongoRepository<StudentskaGrupa, String> {

    @Query("{$and:[{ $expr: { $regexMatch: { input: { $toString: '$oznaka' }, regex: ?0, options: 'i' } } }, " +
            "{ $expr: { $regexMatch: { input: { $toString: '$godina' }, regex: { $cond: [ { $eq: [ '?1', '' ] }, '.*', '^?1$' ] }, options: 'i' } } }," +
            "{ $expr: { $regexMatch: { input: { $toString: '$brojStudenata' }, regex: { $cond: [ { $eq: [ '?2', '' ] }, '.*', '^?2$' ] }, options: 'i' } } }," +
            " {'studijskiProgram': ?3}]}")
    List<StudentskaGrupa> search(String oznaka, String godina, String brojStudenata, String studProgId);
}
