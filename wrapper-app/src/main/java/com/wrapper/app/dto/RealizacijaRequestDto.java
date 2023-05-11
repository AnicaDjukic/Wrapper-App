package com.wrapper.app.dto;

import com.wrapper.app.domain.AsistentZauzece;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @NotBlank
    private String predmetId;

    private String profesorId;

    private List<String> ostaliProfesori;
    
    private List<AsistentZauzeceDto> asistentZauzeca;
}
