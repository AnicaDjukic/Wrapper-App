package com.wrapper.app.mapper;

import com.wrapper.app.domain.Predavac;
import com.wrapper.app.dto.request.PredavacRequestDto;
import com.wrapper.app.dto.generator.PredavacDto;
import com.wrapper.app.service.OrganizacionaJedinicaService;
import org.springframework.stereotype.Component;

@Component
public class PredavacMapper {

    private final OrganizacionaJedinicaService organizacionaJedinicaService;

    public PredavacMapper(OrganizacionaJedinicaService organizacionaJedinicaService) {
        this.organizacionaJedinicaService = organizacionaJedinicaService;
    }

    public Predavac map(PredavacRequestDto dto) {
        Predavac predavac = new Predavac();
        predavac.setOznaka(dto.getOznaka());
        predavac.setIme(dto.getIme());
        predavac.setPrezime(dto.getPrezime());
        predavac.setTitula(dto.getTitula());
        predavac.setDekanat(dto.isDekanat());
        predavac.setOrganizacijaFakulteta(dto.isOrganizacijaFakulteta());
        predavac.setOrgJedinica(organizacionaJedinicaService.getById(dto.getOrgJedinica()));
        return predavac;
    }

    public PredavacDto map(Predavac predavac) {
        return new PredavacDto(
                predavac.getId(),
                predavac.getOznaka(),
                predavac.getIme(),
                predavac.getPrezime(),
                predavac.isOrganizacijaFakulteta(),
                predavac.isDekanat(),
                predavac.getOrgJedinica() != null ? predavac.getOrgJedinica().getId() : "",
                predavac.getTitula()
        );
    }
}
