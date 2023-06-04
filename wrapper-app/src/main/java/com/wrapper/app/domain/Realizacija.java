package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.Realizacija))}")
public class Realizacija {

    @Id
    private String id;
    private String godina;
    private String semestar;
    @DocumentReference
    private List<StudijskiProgramPredmeti> studijskiProgramPredmeti;
}
