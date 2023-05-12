package com.wrapper.app.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@Builder
@Document(collection = "Predavaci")
public class Predavac {

    @Id
    private String id;
    private int oznaka;             // TODO: proveriti jel jedinstveno i sta znaci
    private String ime;
    private String prezime;
    private boolean organizacijaFakulteta;
    private boolean dekanat;

    @DocumentReference
    private OrganizacionaJedinica orgJedinica;
    private String titula;

}
