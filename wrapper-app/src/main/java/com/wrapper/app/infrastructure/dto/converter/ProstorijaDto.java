package com.wrapper.app.infrastructure.dto.converter;

import com.wrapper.app.domain.model.TipProstorije;
import lombok.Data;

import java.util.UUID;

@Data
public class ProstorijaDto {

    private UUID id;
    private String oznaka;
    private TipProstorije tip;
    private int kapacitet;
}
