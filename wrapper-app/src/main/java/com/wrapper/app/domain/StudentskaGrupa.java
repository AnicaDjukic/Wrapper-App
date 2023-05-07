package com.wrapper.app.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "StudentskeGrupe")
public class StudentskaGrupa {

    @Id
    private String id;
    private int oznaka;
    private int godina;
    private String semestar;
    private int brojStudenata;
    private String studijskiProgram;
}
