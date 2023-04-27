package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredavacSearchDto {

    private String oznaka;

    private String ime;

    private String prezime;

    private String orgJedinica;
}
