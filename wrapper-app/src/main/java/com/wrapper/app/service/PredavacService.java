package com.wrapper.app.service;

import com.wrapper.app.domain.OrganizacionaJedinica;
import com.wrapper.app.domain.Predavac;
import com.wrapper.app.dto.PredavacRequestDto;
import com.wrapper.app.dto.PredavacSearchDto;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.PredavacRepository;
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

    public PredavacService(PredavacRepository repository, OrganizacionaJedinicaService organizacionaJedinicaService) {
        this.repository = repository;
        this.organizacionaJedinicaService = organizacionaJedinicaService;
    }

    public Page<Predavac> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Predavac> search(PredavacSearchDto searchDto, Pageable pageable) {
        List<String> orgJedIds = organizacionaJedinicaService.searchByNaziv(searchDto.getOrgJedinica()).stream().map(OrganizacionaJedinica::getId).toList();
        List<Predavac> results = new ArrayList<>();
        orgJedIds.forEach(orgJedId -> results.addAll(repository.search(searchDto.getOznaka(), searchDto.getIme(), searchDto.getPrezime(), orgJedId)));
        return createPage(results, pageable);
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

    public Predavac create(PredavacRequestDto dto) {
        Optional<Predavac> existing = repository.findByOznaka(dto.getOznaka());
        if(existing.isPresent())
            throw new AlreadyExistsException(Predavac.class.getSimpleName());
        Predavac predavac = createPredavac(dto);
        predavac.setId(UUID.randomUUID().toString());
        return repository.save(predavac);
    }

    public Predavac update(String id, PredavacRequestDto dto) {
        if (!repository.existsById(id))
            throw new NotFoundException(Predavac.class.getSimpleName());
        Optional<Predavac> existing = repository.findByOznaka(dto.getOznaka());
        if(existing.isPresent() && !existing.get().getId().equals(id))
            throw new AlreadyExistsException(Predavac.class.getSimpleName());
        Predavac predavac = createPredavac(dto);
        predavac.setId(id);
        return repository.save(predavac);
    }

    private Predavac createPredavac(PredavacRequestDto dto) {
        OrganizacionaJedinica organizacionaJedinica = organizacionaJedinicaService.getById(dto.getOrgJedinica());
        Predavac predavac = Predavac.builder().oznaka(dto.getOznaka())
                .ime(dto.getIme()).prezime(dto.getPrezime())
                .organizacijaFakulteta(dto.isOrganizacijaFakulteta())
                .dekanat(dto.isDekanat())
                .build();
        predavac.setOrgJedinica(organizacionaJedinica);
        return predavac;
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
