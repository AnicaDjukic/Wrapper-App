package com.wrapper.app.domain;

import lombok.Data;

import java.util.List;

@Data
public class PredmetPredavac {

    private String predmetId;
    private String predmetOznaka;
    private int predmetGodina;
    private String profesorId;
    private List<String> ostaliProfesori;
    private List<AsistentZauzece> asistentZauzeca;
}
