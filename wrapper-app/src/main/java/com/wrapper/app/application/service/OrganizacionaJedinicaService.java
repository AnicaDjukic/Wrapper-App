package com.wrapper.app.application.service;

import com.wrapper.app.domain.model.OrganizacionaJedinica;
import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.infrastructure.persistence.repository.OrganizacionaJedinicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizacionaJedinicaService {

    private final OrganizacionaJedinicaRepository repository;

    public OrganizacionaJedinicaService(OrganizacionaJedinicaRepository repository) {
        this.repository = repository;
    }

    public List<OrganizacionaJedinica> getAll() {
        return repository.findAll();
    }

    public OrganizacionaJedinica getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(OrganizacionaJedinica.class.getSimpleName()));
    }

    public List<OrganizacionaJedinica> searchByNaziv(String naziv) {
        return repository.searchByNaziv(naziv);
    }
}
