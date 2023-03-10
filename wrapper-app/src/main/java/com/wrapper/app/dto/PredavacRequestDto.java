package com.wrapper.app.dto;

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
    private String ime;

    // TODO: ne sme biti null ili prazan string
    private String prezime;

    private boolean organizacijaFakulteta;
    private boolean dekanat;

    //TODO: ne sme biti null ili prazan string
    private String orgJedinica;

    // TODO: Opciono -> prazan string ako je nema
    private String titula;
}
