package com.wrapper.app.infrastructure.persistence.repository;

import com.wrapper.app.domain.model.OrganizacionaJedinica;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OrganizacionaJedinicaRepository extends MongoRepository<OrganizacionaJedinica, String> {

    @Query("{'naziv': {$regex : ?0, $options: 'i'}}")
    List<OrganizacionaJedinica> searchByNaziv(String naziv);

    // Get just Katedra or Departman objects
    List<OrganizacionaJedinica> findByDepartmanExists(boolean exists);
}
