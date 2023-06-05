package com.wrapper.app.domain;

import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;
import java.util.Optional;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.Realizacija))}")
public class Realizacija {

    @Id
    private String id;
    private String godina;
    private String semestar;
    @DocumentReference
    private List<StudijskiProgramPredmeti> studijskiProgramPredmeti;

    public void addStudijskiProgram(StudijskiProgramPredmeti studijskiProgram) {
        Optional<StudijskiProgramPredmeti> existing = studijskiProgramPredmeti.stream().filter(
                sp -> sp.getStudijskiProgram().getId().equals(studijskiProgram.getId())).findFirst();
        if (existing.isPresent()) {
            throw new AlreadyExistsException(StudijskiProgramPredmeti.class.getSimpleName());
        }
        studijskiProgramPredmeti.add(studijskiProgram);
    }

    public void removeStudijskiProgram(String studijskiProgramId) {
        StudijskiProgramPredmeti existing = studijskiProgramPredmeti.stream().filter(
                sp -> sp.getStudijskiProgram().getId().equals(studijskiProgramId))
                .findFirst().orElseThrow(() -> new NotFoundException(StudijskiProgram.class.getSimpleName()));
        studijskiProgramPredmeti.remove(existing);
    }
}
