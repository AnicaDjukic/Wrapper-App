package com.wrapper.app.service;

import com.wrapper.app.domain.Departman;
import com.wrapper.app.domain.Katedra;
import com.wrapper.app.domain.Predavac;
import com.wrapper.app.dto.PredavacSearchDto;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.PredavacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Page<Predavac> getAll(Pageable pageable) {
        Page<Predavac> results = repository.findAll(pageable);
        return mapOrgJedinica(results);
    }

    public Page<Predavac> search(PredavacSearchDto searchDto, Pageable pageable) {
        List<String> departmanIds = departmanService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(Departman::getId).toList();
        List<String> katedraIds = katedraService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(Katedra::getId).toList();
        List<Predavac> results = new ArrayList<>();
        departmanIds.forEach(orgJedId -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getIme(), searchDto.getPrezime(), orgJedId)));
        katedraIds.forEach(orgJedId -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getIme(), searchDto.getPrezime(), orgJedId)));
        return mapOrgJedinica(createPage(results, pageable));
    }

    private PageImpl<Predavac> createPage(List<Predavac> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<Predavac> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    private Page<Predavac> mapOrgJedinica(Page<Predavac> list) {
        list.forEach(result -> {
            if(katedraService.existsById(result.getOrgJedinica())) {
                result.setOrgJedinica(katedraService.getById(result.getOrgJedinica()).getNaziv());
            } else {
                Departman departman = departmanService.getById(result.getOrgJedinica());
                result.setOrgJedinica(departman.getNaziv());
            }
        });
        return list;
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
