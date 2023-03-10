package com.wrapper.app.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Meetings")
public class Meeting {

    @Id
    private String id;
    private TipProstorije tipProstorije;
    private MeetingType meetingTip;
    private int brojCasova;
    private String predavac;
    private List<String> ostaliPredavaci;
    private String predmet;
    private List<String> studentskeGrupe;

}
