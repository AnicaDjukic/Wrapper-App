package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PredmetRequestDto {

    private String oznaka;

    // TODO: ne sme biti manje od nule
    private int plan;

    // TODO: ne sme null i prazan string
    private String naziv;

    // TODO: od 1 do 6
    private int godina;

    // TODO: ne sme biti nula ili manje od nule
    private int brojCasovaPred;

    // TODO: ne sme biti nula ili prazan string
    private String studijskiProgram;

    // TODO: Opciono -> prazan string ako nema
    private String sifraStruke;

    // TODO: ne sme biti manje od nule
    private int brojCasovaAud;

    // TODO: ne sme biti nula
    private int brojCasovaLab;

    // TODO: ne sme biti nula
    private int brojCasovaRac;
}
