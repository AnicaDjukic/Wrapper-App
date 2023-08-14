package com.wrapper.app.infrastructure.persistence.repository;

import com.wrapper.app.domain.model.StudijskiProgramPredmeti;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudijskiProgramPredmetiRepository extends MongoRepository<StudijskiProgramPredmeti, String> {

    @Query("{" +
            "  $or: [" +
            "    { 'predmetPredavaci.profesor.id': { $eq: ?0 } }," +
            "    { 'predmetPredavaci.ostaliProfesori.id': { $eq: ?0 } }," +
            "    { 'predmetPredavaci.asistentZauzeca.asistent.id': { $eq: ?0 } }" +
            "  ]" +
            "}")
    List<StudijskiProgramPredmeti> findByPredavacId(String predavacId);
}
