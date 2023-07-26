package com.wrapper.app.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PredmetPredavacDto {

    private String predmetId;
    private int predmetPlan;
    private String predmetOznaka;
    private String predmetNaziv;
    private int predmetGodina;
    private PredavacDto profesor;
    private List<PredavacDto> ostaliProfesori;
    private List<AsistentiZauzecaDto> asistentZauzeca;
    private boolean block;

    public PredmetPredavacDto() {
        this.ostaliProfesori = new ArrayList<>();
        this.asistentZauzeca = new ArrayList<>();
    }
}
