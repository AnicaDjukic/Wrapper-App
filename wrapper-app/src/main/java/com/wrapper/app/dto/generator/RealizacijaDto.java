package com.wrapper.app.dto.generator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RealizacijaDto {

    private String godina;
    private String semestar;
    private List<StudijskiProgramPredmetiDto> studijskiProgramPredmeti;
}
