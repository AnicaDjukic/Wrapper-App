package com.wrapper.app.infrastructure.dto.generator;

import lombok.Data;

import java.util.List;

@Data
public class RealizacijaDto {

    private String godina;
    private String semestar;
    private List<StudijskiProgramPredmetiDto> studijskiProgramPredmeti;
}
