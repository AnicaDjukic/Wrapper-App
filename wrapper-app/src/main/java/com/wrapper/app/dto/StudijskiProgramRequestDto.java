package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StudijskiProgramRequestDto {

    private int stepen;     // TODO: moguce vrednosti: 1, 2
    private int nivo;       // TODO: moguce vrednosti: 1, 2, 5
    private String oznaka;  // TODO: ne sme biti null ili prazan string, mozda i jedinstveno
    private String naziv;   // TODO: ne sme biti null ili prazan string
}
