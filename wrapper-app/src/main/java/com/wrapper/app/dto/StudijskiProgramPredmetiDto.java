package com.wrapper.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StudijskiProgramPredmetiDto {

    private StudijskiProgramDto studijskiProgram;
    private List<PredmetPredavacDto> predmetPredavaci;

    public StudijskiProgramPredmetiDto() {
        predmetPredavaci = new ArrayList<>();
    }
}
