package com.wrapper.app.dto.request;

import com.wrapper.app.dto.request.AsistentZauzeceDto;
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
