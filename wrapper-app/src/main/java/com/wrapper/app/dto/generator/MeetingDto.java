package com.wrapper.app.dto.generator;

import com.wrapper.app.domain.TipProstorije;
import lombok.Data;

import java.util.List;

@Data
public class MeetingDto {

    private String id;
    private TipProstorije tipProstorije;
    private MeetingType meetingTip;
    private int brojCasova;
    private String predavac;
    private List<String> ostaliPredavaci;
    private String predmet;
    private List<String> studentskeGrupe;
    private boolean biWeekly;
}
