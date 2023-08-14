package com.wrapper.app.infrastructure.dto.optimizator;

import com.wrapper.app.infrastructure.dto.generator.MeetingType;
import com.wrapper.app.domain.model.Predavac;
import com.wrapper.app.domain.model.Predmet;
import com.wrapper.app.domain.model.StudentskaGrupa;
import com.wrapper.app.domain.model.TipProstorije;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeetingDto {

    private String id;
    private TipProstorije tipProstorije;
    private MeetingType meetingTip;
    private int brojCasova;
    private Predavac predavac;
    private List<Predavac> ostaliPredavaci;
    private Predmet predmet;
    private List<StudentskaGrupa> studentskeGrupe;
    private boolean biWeekly;
    private int durationInGrains;
    private int requiredCapacity;
}
