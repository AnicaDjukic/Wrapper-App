package com.wrapper.app.dto;

import com.wrapper.app.domain.StudijskiProgramPredmeti;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RealizacijaResponseDto {

    private String id;
    private String godina;
    private String semestar;
    private List<StudijskiProgramPredmeti> studijskiProgramPredmeti;
}
