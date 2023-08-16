package com.wrapper.app.application.mapper;

import com.wrapper.app.presentation.dto.request.ProstorijaRequestDto;
import com.wrapper.app.application.service.OrganizacionaJedinicaService;
import com.wrapper.app.domain.model.OrganizacionaJedinica;
import com.wrapper.app.domain.model.Prostorija;
import com.wrapper.app.infrastructure.dto.generator.ProstorijaDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProstorijaMapper {

    private final OrganizacionaJedinicaService organizacionaJedinicaService;

    public ProstorijaMapper(OrganizacionaJedinicaService organizacionaJedinicaService) {
        this.organizacionaJedinicaService = organizacionaJedinicaService;
    }

    public Prostorija map(ProstorijaRequestDto dto) {
        Prostorija prostorija = new Prostorija();
        prostorija.setOznaka(dto.getOznaka());
        prostorija.setTip(dto.getTip());
        prostorija.setSekundarniTip(!dto.getSekundarniTip().equals("") ? dto.getSekundarniTip() : null);
        prostorija.setKapacitet(dto.getKapacitet());
        prostorija.setOrgJedinica(getOrganizacioneJedinice(dto.getOrgJedinica()));
        prostorija.setSekundarnaOrgJedinica(dto.getSekundarnaOrgJedinica() != null ? getOrganizacioneJedinice(dto.getSekundarnaOrgJedinica()) : null);
        return prostorija;
    }

    private List<OrganizacionaJedinica> getOrganizacioneJedinice(List<String> orgJedinicaIds) {
        List<OrganizacionaJedinica> results = new ArrayList<>();
        for (String orgJedId: orgJedinicaIds) {
            results.add(organizacionaJedinicaService.getById(orgJedId));
        }
        return results;
    }

    public ProstorijaDto map(Prostorija prostorija) {
        return new ProstorijaDto(prostorija.getId(),
                prostorija.getOznaka(),
                prostorija.getOznakaSistem(),
                prostorija.getTip().toString(),
                prostorija.getKapacitet(),
                prostorija.getOrgJedinica().stream().map(OrganizacionaJedinica::getId).toList(),
                prostorija.getSekundarnaOrgJedinica().stream().map(OrganizacionaJedinica::getId).toList(),
                prostorija.getSekundarniTip(),
                prostorija.getOdobreniPredmeti()
        );
    }
}
