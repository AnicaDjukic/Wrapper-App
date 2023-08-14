package com.wrapper.app.infrastructure.dto.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
