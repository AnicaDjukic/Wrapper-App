package com.wrapper.app.presentation.dto.request;

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
public class StudijskiProgramRequestDto {

    @Min(1)
    @Max(2)
    private int stepen;     // TODO: moguce vrednosti: 1, 2

    @Min(1)
    @Max(5)
    private int nivo;       // TODO: moguce vrednosti: 1, 2, 5

    @NotNull
    @NotBlank
    private String oznaka;  // TODO: ne sme biti null ili prazan string, mozda i jedinstveno

    @NotNull
    @NotBlank
    private String naziv;   // TODO: ne sme biti null ili prazan string
}
