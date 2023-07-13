package com.wrapper.app.dto.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PredmetDto {

    private String id;
    private String oznaka;
    private int plan;
    private String naziv;
    private int godina;
    private String semestar;
    private int brojCasovaPred;
    private String studijskiProgram;
    private int brojCasovaVezbe;
    private String sifraStruke;
    private String tipoviNastave;
    private int brojCasovaAud;
    private int brojCasovaLab;
    private int brojCasovaRac;
}
