package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StudentskaGrupaRequestDto {

    // TODO: ne sme manje od nule: prva, druga, ...
    private int oznaka;

    // TODO: moguce vrednosti: 1, 2, 3, 4, 5 i 6
    private int godina;

    // TODO: moguce vrednosti: Z, L
    private String semestar;

    // TODO: veci od nule, jel treba gledati neki max
    private int brojStudenata;

    // TODO: ne sme biti null ili prazan string
    private String studijskiProgram;
}
