package com.wrapper.app.domain;

import com.wrapper.app.exception.NotFoundException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.StudijskiProgramPredmeti))}")
public class StudijskiProgramPredmeti {

    @Id
    private String id;
    @DocumentReference
    private StudijskiProgram studijskiProgram;
    private List<PredmetPredavac> predmetPredavaci;

    public void addPredmet(PredmetPredavac predmetPredavac) {
        predmetPredavac.setBlock(predmetPredavac.isBlock());
        getPredmetPredavaci().add(predmetPredavac);
        studijskiProgram.setBlock(checkPredmeti());
    }

    public void updatePredmet(String predmetId, PredmetPredavac predmetPredavac) {
        PredmetPredavac existing = getPredmetPredavaci().stream()
                .filter(p -> p.getPredmet().getId().equals(predmetId))
                .findFirst().orElseThrow(() -> new NotFoundException(PredmetPredavac.class.getSimpleName()));
        predmetPredavac.setPredmet(existing.getPredmet());
        predmetPredavac.setBlock(predmetPredavac.isBlock());
        int index = getPredmetPredavaci().indexOf(existing);
        getPredmetPredavaci().remove(index);
        getPredmetPredavaci().add(index, predmetPredavac);
        studijskiProgram.setBlock(checkPredmeti());
    }

    public void removePredmet(String predmetId) {
        Optional<PredmetPredavac> predmet = getPredmetPredavaci()
                .stream().filter(spp -> spp.getPredmet().getId().equals(predmetId)).findFirst();
        predmet.ifPresent(predmetPredavac -> getPredmetPredavaci().remove(predmetPredavac));
        studijskiProgram.setBlock(checkPredmeti());
    }

    private boolean checkPredmeti() {
        for (PredmetPredavac predmetPredavac : predmetPredavaci) {
            if (predmetPredavac.isBlock())
                return true;
        }
        return false;
    }


    public void removePredavac(String predavacId) {
        removeProfesor(predavacId);
        removeOstaliProfesor(predavacId);
        removeAsistent(predavacId);
    }

    private void removeProfesor(String predavacId) {
        Optional<PredmetPredavac> predmet = getPredmetPredavaci()
                .stream().filter(spp -> spp.getProfesor().getId().equals(predavacId)).findFirst();
        predmet.ifPresent(predmetPredavac -> predmetPredavac.setProfesor(null));
    }

    private void removeOstaliProfesor(String predavacId) {
        getPredmetPredavaci().forEach(spp -> spp.getOstaliProfesori().removeIf(p -> p.getId().equals(predavacId)));
    }

    private void removeAsistent(String predavacId) {
        getPredmetPredavaci().forEach(spp -> spp.getAsistentZauzeca().removeIf(a -> a.getAsistent().getId().equals(predavacId)));
    }
}
