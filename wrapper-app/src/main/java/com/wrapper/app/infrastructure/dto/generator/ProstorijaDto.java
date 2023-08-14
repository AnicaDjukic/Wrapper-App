package com.wrapper.app.infrastructure.dto.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProstorijaDto {

    private String id;
    private String oznaka;
    private String oznakaSistem;
    private String tip;
    private int kapacitet;
    private List<String> orgJedinica;
    private List<String> sekundarnaOrgJedinica;
    private String sekundarniTip;
    private List<String> odobreniPredmeti;
}
