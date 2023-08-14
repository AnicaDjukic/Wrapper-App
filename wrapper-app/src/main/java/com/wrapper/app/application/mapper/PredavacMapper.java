package com.wrapper.app.application.mapper;

import com.wrapper.app.domain.model.Predavac;
import com.wrapper.app.infrastructure.dto.generator.PredavacDto;
import com.wrapper.app.presentation.dto.request.PredavacRequestDto;
import com.wrapper.app.application.service.OrganizacionaJedinicaService;
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

    public PredavacDto mapToConverterDto(Predavac predavac) {
        return new PredavacDto(
                predavac.getId(),
                predavac.getOznaka(),
                predavac.getIme(),
                predavac.getPrezime(),
                predavac.isOrganizacijaFakulteta(),
                predavac.isDekanat(),
                predavac.getOrgJedinica() != null ? predavac.getOrgJedinica().getId() : null,
                predavac.getTitula()
        );
    }
}
