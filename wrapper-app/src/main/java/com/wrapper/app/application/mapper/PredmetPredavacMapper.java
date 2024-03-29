package com.wrapper.app.application.mapper;

import com.wrapper.app.domain.model.AsistentZauzece;
import com.wrapper.app.domain.model.PredmetPredavac;
import com.wrapper.app.presentation.dto.request.PredavaciDto;
import com.wrapper.app.presentation.dto.request.PredmetPredavaciDto;
import com.wrapper.app.application.service.PredavacService;
import com.wrapper.app.application.service.PredmetService;
import org.springframework.stereotype.Component;

@Component
public class PredmetPredavacMapper {

    private final PredmetService predmetService;

    private final PredavacService predavacService;

    public PredmetPredavacMapper(PredmetService predmetService, PredavacService predavacService) {
        this.predmetService = predmetService;
        this.predavacService = predavacService;
    }

    public PredmetPredavac map(PredmetPredavaciDto dto) {
        PredmetPredavac predmetPredavac = new PredmetPredavac();
        predmetPredavac.setPredmet(predmetService.getById(dto.getPredmetId()));
        return setPredavaci(dto, predmetPredavac);
    }

    public PredmetPredavac map(PredavaciDto dto) {
        PredmetPredavac predmetPredavac = new PredmetPredavac();
        return setPredavaci(dto, predmetPredavac);
    }

    private PredmetPredavac setPredavaci(PredavaciDto dto, PredmetPredavac predmetPredavac) {
        if(dto.getProfesorId() != null) {
            predmetPredavac.setProfesor(predavacService.getById(dto.getProfesorId()));
        }
        dto.getOstaliProfesori().forEach(p -> predmetPredavac.getOstaliProfesori().add(predavacService.getById(p)));
        dto.getAsistentZauzeca().forEach(z -> predmetPredavac.getAsistentZauzeca()
                        .add(new AsistentZauzece(predavacService.getById(z.getAsistentId()), z.getBrojTermina()))
        );
        return predmetPredavac;
    }
}
