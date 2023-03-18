package com.wrapper.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PredmetPredavacDto {

    private String predmetId;
    private String predmetOznaka;
    private String predmetNaziv;
    private int predmetGodina;
    private String profesor;
    private List<String> ostaliProfesori;
    private List<AsistentiZauzecaDto> asistentiZauzeca;

    public PredmetPredavacDto() {
        this.ostaliProfesori = new ArrayList<>();
        this.asistentiZauzeca = new ArrayList<>();
    }
}
