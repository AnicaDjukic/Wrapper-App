package com.wrapper.app.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.model.Prostorija))}")
public class Prostorija {

    @Id
    private String id;
    private String oznaka;
    private String oznakaSistem;
    private TipProstorije tip;
    private int kapacitet;
    @DocumentReference
    private List<OrganizacionaJedinica> orgJedinica = new ArrayList<>();
    @DocumentReference
    private List<OrganizacionaJedinica> sekundarnaOrgJedinica = new ArrayList<>();
    private String sekundarniTip;
    private List<String> odobreniPredmeti = new ArrayList<>();
}
