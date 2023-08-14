package com.wrapper.app.infrastructure.dto.converter;

import lombok.Data;

import java.util.List;

@Data
public class RasporedPrikaz {

    private String nazivRasporeda;
    private List<String> studProgrami;
}
