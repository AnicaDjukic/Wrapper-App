package com.wrapper.app.service;

import com.wrapper.app.domain.Katedra;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.KatedraRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KatedraService {

    private final KatedraRepository repository;

    public KatedraService(KatedraRepository repository) {
        this.repository = repository;
    }

    public Katedra getById(String id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException(Katedra.class.getSimpleName()));
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    public List<Katedra> searchByNaziv(String naziv) {
        return repository.searchByNaziv(naziv);
    }
}
