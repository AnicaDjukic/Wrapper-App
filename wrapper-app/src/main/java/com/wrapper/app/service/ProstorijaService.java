package com.wrapper.app.service;

import com.wrapper.app.domain.Departman;
import com.wrapper.app.domain.Prostorija;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.ProstorijaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProstorijaService {

    private final ProstorijaRepository repository;

    private final KatedraService katedraService;

    private final DepartmanService departmanService;

    public ProstorijaService(ProstorijaRepository repository, KatedraService katedraService, DepartmanService departmanService) {
        this.repository = repository;
        this.katedraService = katedraService;
        this.departmanService = departmanService;
    }

    public Page<Prostorija> getAll(Pageable pageable) {
        Page<Prostorija> results = repository.findAll(pageable);
        results.forEach(result -> {
            List<String> orgJedinice = new ArrayList<>();
            if(result.getOrgJedinica() != null) {
                for (String orgJedId : result.getOrgJedinica()) {
                    if (katedraService.existsById(orgJedId)) {
                        orgJedinice.add(katedraService.getById(orgJedId).getNaziv());
                    } else {
                        if(departmanService.existsById(orgJedId)) {
                            Departman departman = departmanService.getById(orgJedId);
                            orgJedinice.add(departman.getNaziv());
                        } else {
                            throw new NotFoundException("Organizaciona jedinica");
                        }
                    }
                }
                result.setOrgJedinica(orgJedinice);
            }
        });
        return results;
    }

    public Prostorija getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Prostorija.class.getSimpleName()));
    }

    public Prostorija create(Prostorija prostorija) {
        prostorija.getOrgJedinica().forEach(orgJed -> {
            if(!katedraService.existsById(orgJed)) {
                if (!departmanService.existsById(orgJed))
                    throw new NotFoundException("Organizaciona jedinica");
            }
        });
        prostorija.setId(UUID.randomUUID().toString());
        return repository.save(prostorija);
    }

    public Prostorija update(String id, Prostorija prostorija) {
        if (!repository.existsById(id))
            throw new NotFoundException(Prostorija.class.getSimpleName());
        prostorija.getOrgJedinica().forEach(orgJed -> {
            if(!katedraService.existsById(orgJed)) {
                if (!departmanService.existsById(orgJed))
                    throw new NotFoundException("Organizaciona jedinica");
            }
        });
        prostorija.setId(id);
        return repository.save(prostorija);
    }

    public Prostorija deleteById(String id) {
        Prostorija prostorija = getById(id);
        repository.delete(prostorija);
        return prostorija;
    }
}
