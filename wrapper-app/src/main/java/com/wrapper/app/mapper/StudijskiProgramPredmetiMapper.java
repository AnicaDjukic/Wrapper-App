package com.wrapper.app.mapper;

import com.wrapper.app.domain.PredmetPredavac;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import com.wrapper.app.dto.generator.AsistentZauzecaDto;
import com.wrapper.app.dto.generator.PredmetPredavacDto;
import com.wrapper.app.dto.generator.StudijskiProgramPredmetiDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudijskiProgramPredmetiMapper {

    public StudijskiProgramPredmetiDto map(StudijskiProgramPredmeti studijskiProgramPredmeti) {
        StudijskiProgramPredmetiDto studijskiProgramPredmetiDto = new StudijskiProgramPredmetiDto();
        studijskiProgramPredmetiDto.setStudijskiProgramId(studijskiProgramPredmeti.getStudijskiProgram().getId());
        List<PredmetPredavacDto> predmetPredavacDtos = getPredmetPredavacDtos(studijskiProgramPredmeti.getPredmetPredavaci());
        studijskiProgramPredmetiDto.setPredmetPredavaci(predmetPredavacDtos);
        return studijskiProgramPredmetiDto;
    }

    private List<PredmetPredavacDto> getPredmetPredavacDtos(List<PredmetPredavac> predmetPredavaci) {
        List<PredmetPredavacDto> predmetPredavacDtos = new ArrayList<>();
        for (PredmetPredavac predmetPredavac : predmetPredavaci) {
            PredmetPredavacDto predmetPredavacDto = new PredmetPredavacDto();
            predmetPredavacDto.setPredmetId(predmetPredavac.getPredmet().getId());
            predmetPredavacDto.setPredmetPlan(predmetPredavac.getPredmet().getPlan());
            predmetPredavacDto.setPredmetGodina(predmetPredavac.getPredmet().getGodina());
            predmetPredavacDto.setPredmetOznaka(predmetPredavac.getPredmet().getOznaka());
            if(predmetPredavac.getProfesor() != null) {
                predmetPredavacDto.setProfesorId(predmetPredavac.getProfesor().getId());
            }
            predmetPredavac.getOstaliProfesori().forEach(p -> predmetPredavacDto.getOstaliProfesori().add(p.getId()));
            predmetPredavac.getAsistentZauzeca().forEach(a -> {
                AsistentZauzecaDto dto = new AsistentZauzecaDto();
                dto.setAsistentId(a.getAsistent().getId());
                dto.setBrojTermina(a.getBrojTermina());
                predmetPredavacDto.getAsistentZauzeca().add(dto);
            });
            predmetPredavacDtos.add(predmetPredavacDto);
        }
        return predmetPredavacDtos;
    }
}
