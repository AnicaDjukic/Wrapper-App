package com.wrapper.app.dto.optimizator;

import com.wrapper.app.domain.Predavac;
import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.domain.TipProstorije;
import com.wrapper.app.dto.generator.MeetingType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Meeting {

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