package com.wrapper.app.domain;

import com.wrapper.app.dto.RealizacijaRequestDto;
import com.wrapper.app.exception.NotFoundException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Realizacija")
public class Realizacija {

    @Id
    private String id;
    private String godina;
    private String semestar;
    private List<StudijskiProgramPredmeti> studijskiProgramPredmeti;

    public Realizacija update(RealizacijaRequestDto dto) {
        StudijskiProgramPredmeti studijskiProgram =
                studijskiProgramPredmeti.stream()
                        .filter(sp -> sp.getStudijskiProgramId().equals(dto.getStudijskiProgramId()))
                        .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
        PredmetPredavac predmetPredavac =
                studijskiProgram.getPredmetPredavaci().stream()
                        .filter(pp -> pp.getPredmetId().equals(dto.getPredmetId()))
                        .findFirst().orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
        predmetPredavac.setPredmetId(dto.getPredmetId());
        predmetPredavac.setOstaliProfesori(dto.getOstaliProfesori());
        predmetPredavac.setAsistentZauzeca(dto.getAsistentZauzeca());
        return this;
    }
}
