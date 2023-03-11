package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PredmetPredavacDto {

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
