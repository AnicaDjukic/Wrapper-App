package com.wrapper.app.dto;

import com.wrapper.app.domain.TipProstorije;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProstorijaResponseDto {

    private String id;
    private String oznaka;
    private TipProstorije tip;
    private int kapacitet;
    private List<OrganizacionaJedinicaDto> orgJedinica;
}
