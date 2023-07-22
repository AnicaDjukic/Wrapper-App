package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Document(collection = "OrganizacioneJedinice")
public class OrganizacionaJedinica {
    @Id
    private String id;
    private int oznaka;
    private int ssluzbaOznaka;
    private String naziv;
    @DocumentReference
    private OrganizacionaJedinica departman;
}
