package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PredavaciDto {

    private String profesorId;

    private List<String> ostaliProfesori;

    private List<AsistentZauzeceDto> asistentZauzeca;
}
