package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentskaGrupaSearchDto {

    private String oznaka;

    private String godina;

    private String brojStudenata;

    private String studijskiProgram;
}
