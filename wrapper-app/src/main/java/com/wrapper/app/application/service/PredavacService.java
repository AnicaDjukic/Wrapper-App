package com.wrapper.app.application.service;

import com.wrapper.app.presentation.dto.search.PredavacSearchDto;
import com.wrapper.app.domain.model.OrganizacionaJedinica;
import com.wrapper.app.domain.model.Predavac;
import com.wrapper.app.domain.exception.AlreadyExistsException;
import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.infrastructure.persistence.repository.PredavacRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PredavacService {

    private final PredavacRepository repository;

    private final OrganizacionaJedinicaService organizacionaJedinicaService;

    private final StudijskiProgramPredmetiService studijskiProgramPredmetiService;

    public PredavacService(PredavacRepository repository,
                           OrganizacionaJedinicaService organizacionaJedinicaService,
                           StudijskiProgramPredmetiService studijskiProgramPredmetiService) {
        this.repository = repository;
        this.organizacionaJedinicaService = organizacionaJedinicaService;
        this.studijskiProgramPredmetiService = studijskiProgramPredmetiService;
    }

    public Page<Predavac> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Predavac> search(PredavacSearchDto searchDto, Pageable pageable) {
        List<Predavac> results;
        if(searchDto.getOrgJedinica().isEmpty()) {
            results = repository.search(searchDto.getOznaka(), searchDto.getIme(), searchDto.getPrezime());
        } else {
            results = search(searchDto);
        }
        return createPage(results, pageable);
    }

    private List<Predavac> search(PredavacSearchDto searchDto) {
        List<String> orgJedIds = organizacionaJedinicaService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(OrganizacionaJedinica::getId).toList();
        List<Predavac> results = new ArrayList<>();
        orgJedIds.forEach(orgJedId -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getIme(), searchDto.getPrezime(), orgJedId)));
        return results;
    }

    private PageImpl<Predavac> createPage(List<Predavac> results, Pageable pageable) {
        long offset = pageable.getOffset();
        int limit = pageable.getPageSize();
        long endIndex = Math.min(offset + limit, results.size());
        List<Predavac> pageContent = results.subList((int) offset, (int) endIndex);
        return new PageImpl<>(pageContent, pageable, results.size());
    }

    public Predavac getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Predavac.class.getSimpleName()));
    }

    public Predavac create(Predavac predavac) {
        Optional<Predavac> existing = repository.findByOznaka(predavac.getOznaka());
        if(existing.isPresent())
            throw new AlreadyExistsException(Predavac.class.getSimpleName());
        predavac.setId(UUID.randomUUID().toString());
        return repository.save(predavac);
    }

    public Predavac update(String id, Predavac predavac) {
        if (!repository.existsById(id))
            throw new NotFoundException(Predavac.class.getSimpleName());
        Optional<Predavac> existing = repository.findByOznaka(predavac.getOznaka());
        if(existing.isPresent() && !existing.get().getId().equals(id))
            throw new AlreadyExistsException(Predavac.class.getSimpleName());
        predavac.setId(id);
        return repository.save(predavac);
    }

    public void deleteById(String id) {
        studijskiProgramPredmetiService.removePredavac(id);
        repository.deleteById(id);
    }
}
