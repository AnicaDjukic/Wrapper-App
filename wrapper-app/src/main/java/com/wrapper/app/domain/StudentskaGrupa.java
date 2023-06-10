package com.wrapper.app.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Data
@AllArgsConstructor
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.StudentskaGrupa))}")
public class StudentskaGrupa {

    @Id
    private String id;
    private int oznaka;
    private int godina;
    private String semestar;
    private int brojStudenata;
    @DocumentReference
    private StudijskiProgram studijskiProgram;
}
