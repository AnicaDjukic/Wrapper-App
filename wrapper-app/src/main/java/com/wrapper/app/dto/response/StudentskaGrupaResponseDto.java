package com.wrapper.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentskaGrupaResponseDto {

    private String id;
    private int oznaka;
    private int godina;
    private String semestar;
    private int brojStudenata;
    private StudijskiProgramDto studijskiProgram;
}
