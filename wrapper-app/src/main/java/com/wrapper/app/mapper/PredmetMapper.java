package com.wrapper.app.mapper;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.dto.PredmetResponseDto;

public class PredmetMapper {

    public PredmetResponseDto map(Predmet predmet) {
        PredmetResponseDto responseDto = new PredmetResponseDto();
        responseDto.setId(predmet.getId());
        responseDto.setOznaka(predmet.getOznaka());
        responseDto.setPlan(predmet.getPlan());
        responseDto.setNaziv(predmet.getNaziv());
        responseDto.setGodina(predmet.getGodina());
        responseDto.setBrojCasovaPred(predmet.getBrojCasovaPred());
        responseDto.setStudijskiProgram(predmet.getStudijskiProgram().getOznaka()
                + " " + predmet.getStudijskiProgram().getNaziv());
        responseDto.setSifraStruke(predmet.getSifraStruke());
        responseDto.setBrojCasovaAud(predmet.getBrojCasovaAud());
        responseDto.setBrojCasovaLab(predmet.getBrojCasovaLab());
        responseDto.setBrojCasovaRac(predmet.getBrojCasovaRac());
        return responseDto;
    }
}
