package com.wrapper.app.domain;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class StudijskiProgramPredmeti {

    private String studijskiProgramId;
    private List<PredmetPredavac> predmetPredavaci;
}
