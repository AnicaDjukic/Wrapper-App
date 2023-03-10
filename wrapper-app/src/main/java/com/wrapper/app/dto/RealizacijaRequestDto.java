package com.wrapper.app.dto;

import com.wrapper.app.domain.AsistentZauzece;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RealizacijaRequestDto {

    private String studijskiProgramId;
    private String predmetId;
    private String profesorId;
    private List<String> ostaliProfesori;
    private List<AsistentZauzece> asistentZauzeca;
}
