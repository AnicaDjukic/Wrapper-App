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
    private boolean block;      // true - ako je profesorId null ili nema asistenata a postoje vezbe (br. casova vezbi != null)
}
