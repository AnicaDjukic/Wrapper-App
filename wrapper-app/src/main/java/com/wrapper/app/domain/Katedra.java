package com.wrapper.app.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "OrganizacioneJedinice")
public class Katedra extends OrganizacionaJedinica {

    @DocumentReference
    private Departman departman;
}

