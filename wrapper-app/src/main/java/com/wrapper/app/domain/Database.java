package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
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
}
