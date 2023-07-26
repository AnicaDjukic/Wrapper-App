package com.wrapper.app.dto.converter;

import com.wrapper.app.domain.Predavac;
import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.domain.TipProstorije;
import com.wrapper.app.dto.generator.MeetingType;
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
    private PredavacDto predavac;
    private List<PredavacDto> ostaliPredavaci;
    private PredmetDto predmet;
    private List<StudentskaGrupa> studentskeGrupe;
    private boolean biWeekly;
    private int durationInGrains;
    private int requiredCapacity;
}
