package com.wrapper.app.mapper;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.AsistentiZauzecaDto;
import com.wrapper.app.dto.PredmetPredavacDto;
import com.wrapper.app.dto.StudijskiProgramPredmetiDto;
import com.wrapper.app.service.PredavacService;
import com.wrapper.app.service.PredmetService;
import com.wrapper.app.service.StudijskiProgramService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudijskiProgramPredmetiMapper {

    private final StudijskiProgramService studijskiProgramService;

    private final PredmetService predmetService;

    private final PredavacService predavacService;

    public StudijskiProgramPredmetiMapper(StudijskiProgramService studijskiProgramService, PredmetService predmetService, PredavacService predavacService) {
        this.studijskiProgramService = studijskiProgramService;
        this.predmetService = predmetService;
        this.predavacService = predavacService;
    }

    public StudijskiProgramPredmetiDto map(StudijskiProgramPredmeti studijskiProgramPredmeti) {
        StudijskiProgramPredmetiDto studijskiProgramPredmetiDto = new StudijskiProgramPredmetiDto();
        fillStudijskiProgramInfo(studijskiProgramPredmeti.getStudijskiProgramId(), studijskiProgramPredmetiDto);
        for(PredmetPredavac predmetPredavac : studijskiProgramPredmeti.getPredmetPredavaci()) {
            PredmetPredavacDto predmetPredavacDto = new PredmetPredavacDto();
            fillPredmetInfo(predmetPredavac.getPredmetId(), predmetPredavacDto);
            fillProfesorInfo(predmetPredavac.getProfesorId(), predmetPredavacDto);
            fillOstaliProfesoriInfo(predmetPredavac.getOstaliProfesori(), predmetPredavacDto);
            fillAsistentiInfo(predmetPredavac.getAsistentZauzeca(), predmetPredavacDto);
            predmetPredavacDto.setBlock(predmetPredavac.isBlock());
            studijskiProgramPredmetiDto.getPredmetPredavaci().add(predmetPredavacDto);
        }
        return studijskiProgramPredmetiDto;
    }

    private void fillStudijskiProgramInfo(String studijskiProgramId, StudijskiProgramPredmetiDto studijskiProgramPredmetiDto) {
        StudijskiProgram studijskiProgram = studijskiProgramService.getById(studijskiProgramId);
        studijskiProgramPredmetiDto.setStudijskiProgram(studijskiProgram.getOznaka() + " " + studijskiProgram.getNaziv());
    }

    private void fillPredmetInfo(String predmetId, PredmetPredavacDto predmetPredavacDto) {
        Predmet predmet = predmetService.getById(predmetId);
        predmetPredavacDto.setPredmetId(predmet.getId());
        predmetPredavacDto.setPredmetPlan(predmet.getPlan());
        predmetPredavacDto.setPredmetOznaka(predmet.getOznaka());
        predmetPredavacDto.setPredmetNaziv(predmet.getNaziv());
        predmetPredavacDto.setPredmetGodina(predmet.getGodina());
    }

    private void fillProfesorInfo(String profesorId, PredmetPredavacDto predmetPredavacDto) {
        if(profesorId != null && predavacService.existsById(profesorId)) {
            Predavac profesor = predavacService.getById(profesorId);
            String profesorNaziv = (profesor.getTitula() != null ? profesor.getTitula() : "") + " " + profesor.getIme() + " " + profesor.getPrezime();
            predmetPredavacDto.setProfesor(profesorNaziv.trim());
        }
    }

    private void fillOstaliProfesoriInfo(List<String> ostaliProfesori, PredmetPredavacDto predmetPredavacDto) {
        for(String profId : ostaliProfesori) {
            if(!predavacService.existsById(profId)) {
                System.out.println("Ostali profesor: " + profId);
                continue;
            }
            Predavac ostaliProfesor = predavacService.getById(profId);
            String profNaziv = (ostaliProfesor.getTitula() != null ? ostaliProfesor.getTitula() : "") + " " + ostaliProfesor.getIme() + " " + ostaliProfesor.getPrezime();
            predmetPredavacDto.getOstaliProfesori().add(profNaziv.trim());
        }
    }

    private void fillAsistentiInfo(List<AsistentZauzece> asistentiZauzeca, PredmetPredavacDto predmetPredavacDto) {
        for(AsistentZauzece zauzece : asistentiZauzeca) {
            if(!predavacService.existsById(zauzece.getAsistentId())) {
                System.out.println("Asistent: " + zauzece.getAsistentId());
                continue;
            }
            AsistentiZauzecaDto zauzeceDto = new AsistentiZauzecaDto();
            Predavac asistent = predavacService.getById(zauzece.getAsistentId());
            String asistentNaziv = (asistent.getTitula() != null ? asistent.getTitula() : "") + " " + asistent.getIme() + " " + asistent.getPrezime();
            zauzeceDto.setAsistent(asistentNaziv.trim());
            zauzeceDto.setBrojTermina(zauzece.getBrojTermina());
            predmetPredavacDto.getAsistentiZauzeca().add(zauzeceDto);
        }
    }
}
