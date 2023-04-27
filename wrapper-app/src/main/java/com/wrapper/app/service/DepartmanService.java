package com.wrapper.app.service;

import com.wrapper.app.domain.Departman;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.DepartmanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmanService {

    private final DepartmanRepository repository;

    public DepartmanService(DepartmanRepository repository) {
        this.repository = repository;
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public Departman getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Departman.class.getSimpleName()));
    }

    public List<Departman> searchByNaziv(String naziv) {
        return repository.searchByNaziv(naziv);
    }
}
