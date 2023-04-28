package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProstorijaSearchDto {

    private String oznaka;

    private String tip;

    private String kapacitet;

    private String orgJedinica;
}
