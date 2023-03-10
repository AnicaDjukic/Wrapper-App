package com.wrapper.app.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Dani")
public class Dan {

    @Id
    private Long id;
    private int danUNedelji;
}
