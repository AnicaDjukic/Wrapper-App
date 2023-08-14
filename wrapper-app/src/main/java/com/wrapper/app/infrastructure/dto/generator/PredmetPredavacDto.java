package com.wrapper.app.infrastructure.dto.generator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PredmetPredavacDto {

    private String predmetId;
    private String predmetOznaka;
    private int predmetGodina;
    private int predmetPlan;
    private String profesorId;
    private List<String> ostaliProfesori = new ArrayList<>();
    private List<AsistentZauzecaDto> asistentZauzeca = new ArrayList<>();
}
