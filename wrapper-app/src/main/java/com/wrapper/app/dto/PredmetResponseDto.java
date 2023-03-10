package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredmetResponseDto {

    private String id;
    private String oznaka;
    private int plan;
    private String naziv;
    private int godina;
    private int brojCasovaPred;
    private String studijskiProgram;
    private String sifraStruke;
    private int brojCasovaAud;
    private int brojCasovaLab;
    private int brojCasovaRac;
}
