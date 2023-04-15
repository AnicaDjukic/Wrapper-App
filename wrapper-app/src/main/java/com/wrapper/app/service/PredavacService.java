package com.wrapper.app.service;

import com.wrapper.app.domain.Departman;
import com.wrapper.app.domain.Predavac;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.PredavacRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PredavacService {

    private final PredavacRepository repository;

    private final KatedraService katedraService;

    private final DepartmanService departmanService;

    public PredavacService(PredavacRepository repository, KatedraService katedraService, DepartmanService departmanService) {
        this.repository = repository;
        this.katedraService = katedraService;
        this.departmanService = departmanService;
    }

    public List<Predavac> getAll() {
        List<Predavac> results = repository.findAll();
        results.forEach(result -> {
            if(katedraService.existsById(result.getOrgJedinica())) {
                result.setOrgJedinica(katedraService.getById(result.getOrgJedinica()).getNaziv());
            } else {
                Departman departman = departmanService.getById(result.getOrgJedinica());
                result.setOrgJedinica(departman.getNaziv());
            }
        });
        return results;
    }

    public Predavac getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Predavac.class.getSimpleName()));
    }

    public Predavac create(Predavac predavac) {
        if(!katedraService.existsById(predavac.getOrgJedinica())) {
            if(!departmanService.existsById(predavac.getOrgJedinica())) {
                throw new NotFoundException("Organizaciona jedinica");
            }
        }
        predavac.setId(UUID.randomUUID().toString());
        return repository.save(predavac);
    }

    public Predavac update(String id, Predavac predavac) {
        if (!repository.existsById(id))
            throw new NotFoundException(Predavac.class.getSimpleName());
        if(!katedraService.existsById(predavac.getOrgJedinica())) {
            if(!departmanService.existsById(predavac.getOrgJedinica())) {
                throw new NotFoundException("Organizaciona jedinica");
            }
        }
        predavac.setId(id);
        return repository.save(predavac);
    }

    public Predavac deleteById(String id) {
        Predavac predavac = getById(id);
        repository.delete(predavac);
        return predavac;
    }

    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}
