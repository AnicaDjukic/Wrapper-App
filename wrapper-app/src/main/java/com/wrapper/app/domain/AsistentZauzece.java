package com.wrapper.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@AllArgsConstructor
public class AsistentZauzece {

    @DocumentReference
    private Predavac asistent;
    private int brojTermina;
}
