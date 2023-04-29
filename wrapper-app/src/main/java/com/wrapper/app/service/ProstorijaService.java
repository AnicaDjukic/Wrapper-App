package com.wrapper.app.service;

import com.wrapper.app.domain.Departman;
import com.wrapper.app.domain.Katedra;
import com.wrapper.app.domain.Prostorija;
import com.wrapper.app.dto.ProstorijaSearchDto;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.ProstorijaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

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
        List<String> departmanIds = departmanService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(Departman::getId).toList();
        List<String> katedraIds = katedraService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(Katedra::getId).toList();
        List<Prostorija> firstResults = new ArrayList<>();
        departmanIds.forEach(orgJedId -> firstResults.addAll(repository.search(searchDto.getOznaka(), searchDto.getTip(), searchDto.getKapacitet(), orgJedId)));
        List<Prostorija> secondResults = new ArrayList<>();
        katedraIds.forEach(orgJedId -> secondResults.addAll(repository.search(searchDto.getOznaka(), searchDto.getTip(), searchDto.getKapacitet(), orgJedId)));
        // removing duplicates
        Stream<Prostorija> stream = Stream.concat(firstResults.stream(), secondResults.stream());
        return stream.distinct().toList();
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
