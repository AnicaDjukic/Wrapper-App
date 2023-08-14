package com.wrapper.app.application.mapper;

import com.wrapper.app.infrastructure.dto.generator.PredmetDto;
import com.wrapper.app.presentation.dto.request.PredmetRequestDto;
import com.wrapper.app.application.service.StudijskiProgramService;
import com.wrapper.app.domain.model.Predmet;
import org.springframework.stereotype.Component;

@Component
public class PredmetMapper {

    private final StudijskiProgramService studijskiProgramService;

    public PredmetMapper(StudijskiProgramService studijskiProgramService) {
        this.studijskiProgramService = studijskiProgramService;
    }

    public Predmet map(PredmetRequestDto dto) {
        Predmet predmet = new Predmet();
        predmet.setOznaka(dto.getOznaka());
        predmet.setPlan(dto.getPlan());
        predmet.setNaziv(dto.getNaziv());
        predmet.setGodina(dto.getGodina());
        predmet.setBrojCasovaPred(dto.getBrojCasovaPred());
        predmet.setStudijskiProgram(studijskiProgramService.getById(dto.getStudijskiProgram()));
        predmet.setSifraStruke(dto.getSifraStruke());
        predmet.setBrojCasovaAud(dto.getBrojCasovaAud());
        predmet.setBrojCasovaRac(dto.getBrojCasovaRac());
        predmet.setBrojCasovaLab(dto.getBrojCasovaLab());
        predmet.setURealizaciji(false);
        return predmet;
    }

    public PredmetDto map(Predmet predmet) {
        return new PredmetDto(
            predmet.getId(),
            predmet.getOznaka(),
            predmet.getPlan(),
            predmet.getNaziv(),
            predmet.getGodina(),
            predmet.getSemestar(),
            predmet.getBrojCasovaPred(),
            predmet.getStudijskiProgram().getId(),
            predmet.getBrojCasovaVezbe(),
            predmet.getSifraStruke(),
            predmet.getTipoviNastave(),
            predmet.getBrojCasovaAud(),
            predmet.getBrojCasovaLab(),
            predmet.getBrojCasovaRac()
        );
    }

    public PredmetDto mapToConverterDto(Predmet predmet) {
        return new PredmetDto(
                predmet.getId(),
                predmet.getOznaka(),
                predmet.getPlan(),
                predmet.getNaziv(),
                predmet.getGodina(),
                predmet.getSemestar(),
                predmet.getBrojCasovaPred(),
                predmet.getStudijskiProgram() != null ? predmet.getStudijskiProgram().getId() : null,
                predmet.getBrojCasovaVezbe(),
                predmet.getSifraStruke(),
                predmet.getTipoviNastave(),
                predmet.getBrojCasovaAud(),
                predmet.getBrojCasovaLab(),
                predmet.getBrojCasovaRac()
        );
    }
}
