package com.wrapper.app.infrastructure.dto.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredmetDto {

    private String id;
    private String oznaka;              // TODO: proveriti jel jedinstveno - NIJE -> primer: RI4A
    private int plan;
    private String naziv;
    private int godina;
    private String semestar;            // TODO: rekla Eva suvisno
    private int brojCasovaPred;
    private String studijskiProgram;
    private int brojCasovaVezbe;
    private String sifraStruke;
    private String tipoviNastave;       // TODO: rekla Eva izbaciti
    private int brojCasovaAud;
    private int brojCasovaLab;
    private int brojCasovaRac;
}
