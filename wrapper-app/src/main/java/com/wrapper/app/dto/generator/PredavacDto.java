package com.wrapper.app.dto.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PredavacDto {

    private String id;
    private int oznaka;
    private String ime;
    private String prezime;
    private boolean organizacijaFakulteta;
    private boolean dekanat;
    private String orgJedinica;
    private String titula;
}
