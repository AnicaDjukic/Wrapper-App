package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudijskiProgramResponseDto {

    private String id;
    private int stepen;
    private int nivo;
    private String stepenStudija;
    private String oznaka;
    private String naziv;
    private boolean block;
}
