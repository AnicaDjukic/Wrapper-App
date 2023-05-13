package com.wrapper.app.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Builder
//@Document(collection = "Predmeti")
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.Predmet))}")
public class Predmet {

    @Id
    private String id;
    private String oznaka;              // TODO: proveriti jel jedinstveno - NIJE -> primer: RI4A
    private int plan;
    private String naziv;
    private int godina;
    private String semestar;            // TODO: rekla Eva suvisno
    private int brojCasovaPred;

    @DocumentReference
    private StudijskiProgram studijskiProgram;

    private int brojCasovaVezbe;
    private String sifraStruke;
    private String tipoviNastave;       // TODO: rekla Eva izbaciti
    private int brojCasovaAud;
    private int brojCasovaLab;
    private int brojCasovaRac;
    private boolean uRealizaciji;
}
