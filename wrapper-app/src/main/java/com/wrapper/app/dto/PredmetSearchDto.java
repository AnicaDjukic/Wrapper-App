package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredmetSearchDto {

    private String oznaka;

    private String naziv;

    private String studijskiProgram;
}
