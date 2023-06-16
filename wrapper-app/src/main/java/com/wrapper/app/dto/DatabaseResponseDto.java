package com.wrapper.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatabaseResponseDto {

    private String id;

    private String semestar;

    private String godina;

    private String predmeti;

    private String studijskiProgrami;

    private String studentskeGrupe;

    private String realizacija;

    private String predavaci;

    private String prostorije;

    private Date generationStarted;

    private Date generationFinished;

    private String path;
}
