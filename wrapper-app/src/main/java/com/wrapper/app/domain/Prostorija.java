package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.Prostorija))}")
public class Prostorija {

    @Id
    private String id;
    private String oznaka;  // TODO: oznaka jedinstvena?
    private TipProstorije tip;
    private int kapacitet;
    private List<String> orgJedinica;   // katedra ili departman

}
