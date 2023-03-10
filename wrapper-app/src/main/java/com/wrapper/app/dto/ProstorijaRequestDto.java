package com.wrapper.app.dto;

import com.wrapper.app.domain.TipProstorije;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProstorijaRequestDto {

    // TODO: ne sme biti null ili prazan string
    private String oznaka;

    // TODO: mora biti jedan od navedenih tipova
    private TipProstorije tip;

    // TODO: veci od nule
    private int kapacitet;

    private List<String> orgJedinica;
}
