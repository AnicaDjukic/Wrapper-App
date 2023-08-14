package com.wrapper.app.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredavacResponseDto {

    private String id;
    private int oznaka;
    private String ime;
    private String prezime;
    private boolean organizacijaFakulteta;
    private boolean dekanat;
    private String orgJedinica; // katedra
    private String titula;
}
