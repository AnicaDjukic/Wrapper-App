package com.wrapper.app.infrastructure.dto.generator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StudentskaGrupaDto {

    private String id;
    private int oznaka;
    private int godina;
    private String semestar;
    private int brojStudenata;
    private String studijskiProgram;
}
