package com.wrapper.app.mapper;

import com.wrapper.app.domain.OrganizacionaJedinica;
import com.wrapper.app.domain.Prostorija;
import com.wrapper.app.dto.ProstorijaRequestDto;
import com.wrapper.app.dto.generator.ProstorijaDto;
import com.wrapper.app.service.OrganizacionaJedinicaService;
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
        prostorija.setKapacitet(dto.getKapacitet());
        prostorija.setOrgJedinica(getOrganizacioneJedinice(dto.getOrgJedinica()));
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
