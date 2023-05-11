package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Predmeti")
public class Predmet {

    @Id
    private String id;
    private String oznaka;              // TODO: proveriti jel jedinstveno - NIJE -> primer: RI4A
    private int plan;
    private String naziv;
    private int godina;                 // TODO: rekla Eva suvisno ali mi treba
    private String semestar;            // TODO: rekla Eva suvisno
    private int brojCasovaPred;
    private String studijskiProgram;    // TODO: rekla Eva suvisno ali mi treba
    private int brojCasovaVezbe;
    private String sifraStruke;
    private String tipoviNastave;       // TODO: rekla Eva izbaciti
    private int brojCasovaAud;          // TODO: proveriti jel moze 0 umesto -1
    private int brojCasovaLab;
    private int brojCasovaRac;
    private boolean uRealizaciji;
}
