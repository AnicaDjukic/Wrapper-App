package com.wrapper.app.domain;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Predavaci")
public class Predavac {

    @Id
    private String id;
    private int oznaka;             // TODO: proveriti jel jedinstveno i sta znaci
    private String ime;
    private String prezime;
    private boolean organizacijaFakulteta;
    private boolean dekanat;
    private String orgJedinica;     // TODO: katedra
    private String titula;

}
