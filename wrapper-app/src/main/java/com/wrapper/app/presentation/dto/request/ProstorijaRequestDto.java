package com.wrapper.app.presentation.dto.request;

import com.wrapper.app.domain.model.TipProstorije;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProstorijaRequestDto {

    // TODO: ne sme biti null ili prazan string
    @NotNull
    @NotBlank
    private String oznaka;

    // TODO: mora biti jedan od navedenih tipova
    @NotNull
    private TipProstorije tip;

    // TODO: veci od nule
    @Min(1)
    private int kapacitet;

    private List<String> orgJedinica;
}
