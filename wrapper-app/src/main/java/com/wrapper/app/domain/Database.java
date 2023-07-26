package com.wrapper.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@EqualsAndHashCode
@Document(collection = "Databases")
public class Database {

    @Id
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
