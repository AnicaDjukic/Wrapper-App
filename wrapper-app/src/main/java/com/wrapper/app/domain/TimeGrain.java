package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "TimeGrains")
public class TimeGrain {

    @Id
    private String id;
    private int grainIndex;
    private int pocetniMinut;
    private int dan;
}
