package com.wrapper.app.domain.model;

import com.wrapper.app.infrastructure.dto.generator.MeetingType;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "#{@collectionNameResolver.resolveCollectionName(T(com.wrapper.app.domain.model.Meeting))}")
public class Meeting {

    @Id
    private String id;
    private TipProstorije tipProstorije;
    private MeetingType meetingTip;
    private int brojCasova;
    private Predavac predavac;
    private List<Predavac> ostaliPredavaci;
    private Predmet predmet;
    private List<StudentskaGrupa> studentskeGrupe;
    private boolean biWeekly;
}
