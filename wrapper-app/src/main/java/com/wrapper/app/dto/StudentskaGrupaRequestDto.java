package com.wrapper.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class StudentskaGrupaRequestDto {

    // TODO: ne sme manje od nule: prva, druga, ...
    @Min(1)
    private int oznaka;

    // TODO: moguce vrednosti: 1, 2, 3, 4, 5 i 6
    @Min(1)
    @Max(6)
    private int godina;

    // TODO: moguce vrednosti: Z, L
    @NotNull
    @NotBlank
    private String semestar;

    // TODO: veci od nule, jel treba gledati neki max
    @Min(1)
    private int brojStudenata;

    // TODO: ne sme biti null ili prazan string
    @NotNull
    @NotBlank
    private String studijskiProgram;
}
