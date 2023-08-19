package com.wrapper.app.domain.model;

import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.domain.cascade.CascadeDelete;
import com.wrapper.app.domain.cascade.CascadeSave;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.model.StudijskiProgramPredmeti))}")
public class StudijskiProgramPredmeti {

    @Id
    private String id;

    @CascadeDelete
    @CascadeSave
    @DocumentReference
    private StudijskiProgram studijskiProgram;

    @CascadeDelete
    @CascadeSave
    private List<PredmetPredavac> predmetPredavaci;

    public void addPredmetPredavac(PredmetPredavac predmetPredavac) {
        predmetPredavac.setBlock(predmetPredavac.isBlock());
        predmetPredavac.getPredmet().setURealizaciji(true);
        predmetPredavac.getPredmet().setStudijskiProgram(studijskiProgram);
        predmetPredavaci.add(predmetPredavac);
        updateBlockStatus();
    }

    public void updatePredmetPredavac(String predmetId, PredmetPredavac predmetPredavac) {
        PredmetPredavac existing = predmetPredavaci.stream().filter(p -> p.getPredmet().getId().equals(predmetId))
                .findFirst().orElseThrow(() -> new NotFoundException(PredmetPredavac.class.getSimpleName()));
        predmetPredavac.setPredmet(existing.getPredmet());
        predmetPredavac.setBlock(predmetPredavac.isBlock());
        int index = predmetPredavaci.indexOf(existing);
        predmetPredavaci.remove(index);
        predmetPredavaci.add(index, predmetPredavac);
        updateBlockStatus();
    }

    public void removePredmet(String predmetId) {
        Predmet predmet = predmetPredavaci.stream()
                .filter(spp -> spp.getPredmet().getId().equals(predmetId)).findFirst()
                .orElseThrow(() -> new NotFoundException(PredmetPredavac.class.getSimpleName())).getPredmet();
        predmet.setURealizaciji(false);
    }

    public void removePredmetPredavac(String predmetId) {
        PredmetPredavac predmetPredavac = predmetPredavaci.stream()
                .filter(spp -> spp.getPredmet().getId().equals(predmetId)).findFirst()
                .orElseThrow(() -> new NotFoundException(PredmetPredavac.class.getSimpleName()));
        predmetPredavaci.remove(predmetPredavac);
        updateBlockStatus();
    }

    public void updateBlockStatus() {
        for (PredmetPredavac predmetPredavac : predmetPredavaci) {
            if (predmetPredavac.isBlock()) {
                studijskiProgram.setBlock(true);
                return;
            }
        }
        studijskiProgram.setBlock(false);
    }

    public void removeMissingPredavaci() {
        for(PredmetPredavac predmetPredavac : predmetPredavaci) {
            predmetPredavac.getOstaliProfesori().removeIf(Objects::isNull);
            predmetPredavac.removeMissingAsistenti();
            updateBlockStatus();
        }
    }

    public void removePredavac(String predavacId) {
        removeProfesor(predavacId);
        removeOstaliProfesor(predavacId);
        removeAsistent(predavacId);
        updateBlockStatus();
    }

    private void removeProfesor(String predavacId) {
        Optional<PredmetPredavac> predmet = predmetPredavaci.stream().filter(spp -> {
            if (spp.getProfesor() != null) {
                return spp.getProfesor().getId().equals(predavacId);
            }
            return true;
        }).findFirst();
        predmet.ifPresent(predmetPredavac -> predmetPredavac.setProfesor(null));
    }

    private void removeOstaliProfesor(String predavacId) {
        predmetPredavaci.forEach(spp -> spp.getOstaliProfesori().removeIf(p -> {
            if (p != null) {
                return p.getId().equals(predavacId);
            }
            return true;
        }));
    }

    private void removeAsistent(String predavacId) {
        predmetPredavaci.forEach(spp -> spp.getAsistentZauzeca().removeIf(a -> {
            if (a.getAsistent() != null) {
                return a.getAsistent().getId().equals(predavacId);
            }
            return true;
        }));
    }
}
