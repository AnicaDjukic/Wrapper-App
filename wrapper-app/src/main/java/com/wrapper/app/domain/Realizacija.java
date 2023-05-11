package com.wrapper.app.domain;

import com.wrapper.app.dto.RealizacijaRequestDto;
import com.wrapper.app.dto.RealizacijaPredavaciDto;
import com.wrapper.app.exception.NotFoundException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "Realizacija")
public class Realizacija {

    @Id
    private String id;
    private String godina;
    private String semestar;
    private List<StudijskiProgramPredmeti> studijskiProgramPredmeti;

//    public Realizacija update(RealizacijaRequestDto dto) {
//        StudijskiProgramPredmeti studijskiProgram =
//                studijskiProgramPredmeti.stream()
//                        .filter(sp -> sp.getStudijskiProgramId().equals(dto.getStudijskiProgramId()))
//                        .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
//        PredmetPredavac predmetPredavac =
//                studijskiProgram.getPredmetPredavaci().stream()
//                        .filter(pp -> pp.getPredmetId().equals(dto.getPredmetId()))
//                        .findFirst().orElseThrow(() -> new NotFoundException(Predmet.class.getSimpleName()));
//        predmetPredavac.setPredmetId(dto.getPredmetId());
//        predmetPredavac.setOstaliProfesori(dto.getOstaliProfesori());
//        predmetPredavac.setAsistentZauzeca(dto.getAsistentZauzeca());
//        return this;
//    }

    public Realizacija addPredmet(String studijskiProgramId, PredmetPredavac predmetPredavac) {
        StudijskiProgramPredmeti studijskiProgram =
                studijskiProgramPredmeti.stream()
                        .filter(sp -> sp.getStudijskiProgramId().equals(studijskiProgramId))
                        .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
        studijskiProgram.getPredmetPredavaci().add(predmetPredavac);
        return this;
    }

    public Realizacija updatePredmet(String studijskiProgramId, String predmetId, PredmetPredavac predmetPredavac) {
        StudijskiProgramPredmeti studijskiProgram =
                studijskiProgramPredmeti.stream()
                        .filter(sp -> sp.getStudijskiProgramId().equals(studijskiProgramId))
                        .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
        PredmetPredavac predmet = studijskiProgram.getPredmetPredavaci().stream()
                .filter(p -> p.getPredmetId().equals(predmetId))
                .findFirst().orElseThrow(() -> new NotFoundException(PredmetPredavac.class.getSimpleName()));
        int index = studijskiProgram.getPredmetPredavaci().indexOf(predmet);
        studijskiProgram.getPredmetPredavaci().remove(index);
        studijskiProgram.getPredmetPredavaci().add(index, predmetPredavac);
        return this;
    }

    public Realizacija removePredmet(String predmetId) {
        for(StudijskiProgramPredmeti studijskiProgramPredmet : studijskiProgramPredmeti){
            Optional<PredmetPredavac> predmet = studijskiProgramPredmet.getPredmetPredavaci()
                    .stream().filter(spp -> spp.getPredmetId().equals(predmetId)).findFirst();
            if(predmet.isPresent()) {
                studijskiProgramPredmet.getPredmetPredavaci().remove(predmet.get());
                break;
            }
        }
        return this;
    }

    public Realizacija removePredavac(String predavacId) {
        studijskiProgramPredmeti.forEach(sp -> {
            removeProfesor(sp, predavacId);
            removeOstaliProfesor(sp, predavacId);
            removeAsistent(sp, predavacId);
        });
        return this;
    }

    private void removeProfesor(StudijskiProgramPredmeti studijskiProgramPredmet, String predavacId) {
        Optional<PredmetPredavac> predmet = studijskiProgramPredmet.getPredmetPredavaci()
                .stream().filter(spp -> spp.getProfesorId().equals(predavacId)).findFirst();
        predmet.ifPresent(predmetPredavac -> predmetPredavac.setProfesorId(""));
    }

    private void removeOstaliProfesor(StudijskiProgramPredmeti studijskiProgramPredmet, String predavacId) {
        Optional<PredmetPredavac> predmet = studijskiProgramPredmet.getPredmetPredavaci()
                .stream().filter(spp -> spp.getOstaliProfesori().contains(predavacId)).findFirst();
        predmet.ifPresent(predmetPredavac -> predmetPredavac.getOstaliProfesori().remove(predavacId));
    }

    private void removeAsistent(StudijskiProgramPredmeti studijskiProgramPredmet, String predavacId) {
        studijskiProgramPredmet.getPredmetPredavaci()
                .forEach(spp -> spp.getAsistentZauzeca().removeIf(a -> a.getAsistentId().equals(predavacId)));
    }

    public Realizacija removeStudijskiProgram(String studProgramId) {
        studijskiProgramPredmeti.removeIf(sp -> sp.getStudijskiProgramId().equals(studProgramId));
        return this;
    }
}
