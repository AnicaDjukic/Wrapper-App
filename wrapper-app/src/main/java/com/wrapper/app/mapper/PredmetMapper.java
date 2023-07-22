package com.wrapper.app.mapper;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.dto.request.PredmetRequestDto;
import com.wrapper.app.dto.generator.PredmetDto;
import com.wrapper.app.service.StudijskiProgramService;
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
}
