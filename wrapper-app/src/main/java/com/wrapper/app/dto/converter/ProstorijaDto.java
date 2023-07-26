package com.wrapper.app.dto.converter;

import com.wrapper.app.domain.TipProstorije;
import lombok.Data;

import java.util.UUID;

@Data
public class ProstorijaDto {

    private UUID id;
    private String oznaka;
    private TipProstorije tip;
    private int kapacitet;
}
