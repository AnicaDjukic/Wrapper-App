package com.wrapper.app.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatabaseRequestDto {

    private String semestar;

    private String godina;

    private String predmeti;

    private String studijskiProgrami;

    private String studentskeGrupe;

    private String realizacija;

    private String predavaci;

    private String prostorije;
}
