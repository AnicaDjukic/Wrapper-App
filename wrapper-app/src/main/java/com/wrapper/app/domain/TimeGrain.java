package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.UUID;

@Data
@Document(collection = "TimeGrains")
public class TimeGrain {

    @Id
    private UUID id;

    private int grainIndex; // unique

    private int pocetniMinut;

    @DocumentReference
    private Dan dan;
}
