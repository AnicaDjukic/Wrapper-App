package com.wrapper.app.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PredmetRequestDto {

    @NotBlank
    @NotNull
    private String oznaka;

    // TODO: ne sme biti manje od nule - ja stavila da ne sme biti ni 0
    @Min(value = 1)
    private int plan;

    // TODO: ne sme null i prazan string
    @NotBlank
    @NotNull
    private String naziv;

    // TODO: od 1 do 6
    @Min(value = 1)
    @Max(value = 6)
    private int godina;

    // TODO: ne sme biti manje od nule
    @Min(value = 0)
    private int brojCasovaPred;

    // TODO: ne sme biti nula ili prazan string
    @NotBlank
    @NotNull
    private String studijskiProgram;

    // TODO: Opciono -> prazan string ako nema
    private String sifraStruke;

    // TODO: ne sme biti manje od nule
    @Min(value = 0)
    private int brojCasovaAud;

    // TODO: ne sme biti nula - manje od nule valjda
    @Min(value = 0)
    private int brojCasovaLab;

    // TODO: ne sme biti nula ' manje od nule valjda
    @Min(value = 0)
    private int brojCasovaRac;
}
