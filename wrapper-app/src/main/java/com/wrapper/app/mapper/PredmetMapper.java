package com.wrapper.app.mapper;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.dto.PredmetResponseDto;

public class PredmetMapper {

    public PredmetResponseDto map(Predmet predmet) {
        PredmetResponseDto predmetResponseDto = new PredmetResponseDto();
        predmetResponseDto.setId(predmet.getId());
        predmetResponseDto.setOznaka(predmet.getOznaka());
        predmetResponseDto.setPlan(predmet.getPlan());
        predmetResponseDto.setNaziv(predmet.getNaziv());
        predmetResponseDto.setGodina(predmet.getGodina());
        predmetResponseDto.setBrojCasovaPred(predmet.getBrojCasovaPred());
        predmetResponseDto.setStudijskiProgram(predmet.getStudijskiProgram().getOznaka()
                + " " + predmet.getStudijskiProgram().getNaziv());
        predmetResponseDto.setSifraStruke(predmet.getSifraStruke());
        predmetResponseDto.setBrojCasovaAud(predmet.getBrojCasovaAud());
        predmetResponseDto.setBrojCasovaLab(predmet.getBrojCasovaLab());
        predmetResponseDto.setBrojCasovaRac(predmet.getBrojCasovaRac());
        return predmetResponseDto;
    }
}
