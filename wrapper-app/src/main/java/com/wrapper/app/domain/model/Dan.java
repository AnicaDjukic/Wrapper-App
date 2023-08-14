package com.wrapper.app.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Dan")
public class Dan {

    @Id
    private Long id;

    private int danUNedelji;
}
