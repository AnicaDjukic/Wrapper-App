package com.wrapper.app.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PredavacRequestDto {

    //TODO: proveriti jel treba neko ogranicenje
    private int oznaka;

    // TODO: ne sme biti null ili prazan string
    @NotNull
    @NotBlank
    private String ime;

    // TODO: ne sme biti null ili prazan string
    @NotNull
    @NotBlank
    private String prezime;

    private boolean organizacijaFakulteta;
    private boolean dekanat;

    // TODO: ne sme biti null ili prazan string
    @NotNull
    @NotBlank
    private String orgJedinica;

    // TODO: Opciono -> prazan string ako je nema
    private String titula;
}
