package com.wrapper.app.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.Prostorija))}")
public class Prostorija {

    @Id
    private String id;
    private String oznaka;  // TODO: oznaka jedinstvena?
    private TipProstorije tip;
    private int kapacitet;
    @DocumentReference
    private List<OrganizacionaJedinica> orgJedinica;   // katedra ili departman
}
