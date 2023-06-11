package com.wrapper.app.domain;

import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.cascade.CascadeDelete;
import com.wrapper.app.repository.cascade.CascadeSave;
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
        studijskiProgram.setBlock(checkPredmeti());
    }

    public void updatePredmetPredavac(String predmetId, PredmetPredavac predmetPredavac) {
        PredmetPredavac existing = predmetPredavaci.stream().filter(p -> p.getPredmet().getId().equals(predmetId)).findFirst().orElseThrow(() -> new NotFoundException(PredmetPredavac.class.getSimpleName()));
        predmetPredavac.setPredmet(existing.getPredmet());
        predmetPredavac.setBlock(predmetPredavac.isBlock());
        int index = predmetPredavaci.indexOf(existing);
        predmetPredavaci.remove(index);
        predmetPredavaci.add(index, predmetPredavac);
        studijskiProgram.setBlock(checkPredmeti());
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
        studijskiProgram.setBlock(checkPredmeti());
    }

    private boolean checkPredmeti() {
        for (PredmetPredavac predmetPredavac : predmetPredavaci) {
            if (predmetPredavac.isBlock()) return true;
        }
        return false;
    }

    public void removePredavac(String predavacId) {
        removeProfesor(predavacId);
        removeOstaliProfesor(predavacId);
        removeAsistent(predavacId);
        studijskiProgram.setBlock(checkPredmeti());
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
