package com.wrapper.app.dto.generator;

import lombok.Data;

import java.util.List;

@Data
public class StudijskiProgramPredmetiDto {

    private String studijskiProgramId;
    private List<PredmetPredavacDto> predmetPredavaci;
}
