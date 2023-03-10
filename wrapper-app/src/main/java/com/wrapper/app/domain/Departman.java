package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Departmani")
public class Departman {

    @Id
    private String id;
    private int oznaka;
    private int ssluzbaOznaka;
    private String naziv;

}
