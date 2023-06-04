package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
public class PredmetPredavac {

    @DocumentReference
    private Predmet predmet;
    @DocumentReference
    private Predavac profesor;
    @DocumentReference
    private List<Predavac> ostaliProfesori;
    private List<AsistentZauzece> asistentZauzeca;
    private boolean block;      // true - ako je profesorId null ili nema asistenata a postoje vezbe (br. casova vezbi != null)

    public PredmetPredavac() {
        ostaliProfesori = new ArrayList<>();
        asistentZauzeca = new ArrayList<>();
    }

    public boolean isBlock() {
        boolean profesorAlert = predmet.getBrojCasovaPred() != 0 && profesor == null;
        boolean asistentiAlert = (predmet.getBrojCasovaAud() != 0 || predmet.getBrojCasovaLab() != 0 || predmet.getBrojCasovaRac() != 0)
                && asistentZauzeca.size() == 0;
        return profesorAlert || asistentiAlert;
    }
}
