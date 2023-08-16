package com.wrapper.app.presentation.dto.response;

import com.wrapper.app.domain.model.OrganizacionaJedinica;
import com.wrapper.app.domain.model.TipProstorije;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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
    private String sekundarniTip;
    private List<OrganizacionaJedinica> sekundarnaOrgJedinica;
}
