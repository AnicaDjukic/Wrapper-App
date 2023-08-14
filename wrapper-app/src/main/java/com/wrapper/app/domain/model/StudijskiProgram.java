package com.wrapper.app.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.model.StudijskiProgram))}")
public class StudijskiProgram {

    @Id
    private String id;
    private int stepen;
    private int nivo;
    private String oznaka;
    private String naziv;
    private boolean block;
}
