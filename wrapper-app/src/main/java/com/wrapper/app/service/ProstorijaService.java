package com.wrapper.app.service;

import com.wrapper.app.domain.OrganizacionaJedinica;
import com.wrapper.app.domain.Prostorija;
import com.wrapper.app.dto.ProstorijaSearchDto;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.ProstorijaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProstorijaService {

    private final ProstorijaRepository repository;

    private final OrganizacionaJedinicaService organizacionaJedinicaService;

    public ProstorijaService(ProstorijaRepository repository, OrganizacionaJedinicaService organizacionaJedinicaService) {
        this.repository = repository;
        this.organizacionaJedinicaService = organizacionaJedinicaService;
    }

    public Page<Prostorija> getAll(Pageable pageable) {
        Page<Prostorija> results = repository.findAll(pageable);
        return mapOrgJedinice(results);
    }

    public Page<Prostorija> search(ProstorijaSearchDto searchDto, Pageable pageable) {
        List<Prostorija> results;
        if(searchDto.getOrgJedinica().isEmpty()) {
            results = repository.searchWithoutOrgJedinica(searchDto.getOznaka(), searchDto.getTip(), searchDto.getKapacitet());
        } else {
            results = search(searchDto);
        }
        Page<Prostorija> page = createPage(results, pageable);
        return mapOrgJedinice(page);
    }

    private List<Prostorija> search(ProstorijaSearchDto searchDto) {
        List<String> orgJedinicaIds = organizacionaJedinicaService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(OrganizacionaJedinica::getId).toList();
        List<Prostorija> results = new ArrayList<>();
        orgJedinicaIds.forEach(orgJedId -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getTip(), searchDto.getKapacitet(), orgJedId)));
        return results;
    }

    private PageImpl<Prostorija> createPage(List<Prostorija> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<Prostorija> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    private Page<Prostorija> mapOrgJedinice(Page<Prostorija> results) {
        results.forEach(result -> {
            List<String> orgJedinice = new ArrayList<>();
            if(result.getOrgJedinica() != null) {
                for (String orgJedId : result.getOrgJedinica()) {
                    if (organizacionaJedinicaService.existsById(orgJedId)) {
                        orgJedinice.add(organizacionaJedinicaService.getById(orgJedId).getNaziv());
                    } else {
                        throw new NotFoundException(OrganizacionaJedinica.class.getSimpleName());
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
        validate(prostorija);
        prostorija.setId(UUID.randomUUID().toString());
        return repository.save(prostorija);
    }

    private void validate(Prostorija prostorija) {
        prostorija.getOrgJedinica().forEach(orgJed -> {
            if(!organizacionaJedinicaService.existsById(orgJed)) {
                throw new NotFoundException(OrganizacionaJedinica.class.getSimpleName());
            }
        });
        Optional<Prostorija> existing = repository.findByOznaka(prostorija.getOznaka());
        if(existing.isPresent())
            throw new AlreadyExistsException(Prostorija.class.getSimpleName());
    }

    public Prostorija update(String id, Prostorija prostorija) {
        validate(id, prostorija);
        prostorija.setId(id);
        return repository.save(prostorija);
    }

    private void validate(String id, Prostorija prostorija) {
        if (!repository.existsById(id))
            throw new NotFoundException(Prostorija.class.getSimpleName());
        prostorija.getOrgJedinica().forEach(orgJed -> {
            if(!organizacionaJedinicaService.existsById(orgJed)) {
                throw new NotFoundException(OrganizacionaJedinica.class.getSimpleName());
            }
        });
        Optional<Prostorija> existing = repository.findByOznaka(prostorija.getOznaka());
        if(existing.isPresent() && !existing.get().getId().equals(id))
            throw new AlreadyExistsException(Prostorija.class.getSimpleName());
    }

    public Prostorija deleteById(String id) {
        Prostorija prostorija = getById(id);
        repository.delete(prostorija);
        return prostorija;
    }
}
