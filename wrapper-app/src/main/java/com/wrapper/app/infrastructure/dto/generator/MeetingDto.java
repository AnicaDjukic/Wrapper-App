package com.wrapper.app.infrastructure.dto.generator;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrapper.app.domain.model.TipProstorije;
import lombok.Data;

import java.util.ArrayList;
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

    @JsonCreator
    public MeetingDto(
            @JsonProperty("id") String id,
            @JsonProperty("tipProstorije") TipProstorije tipProstorije,
            @JsonProperty("meetingTip") MeetingType meetingTip,
            @JsonProperty("brojCasova") int brojCasova,
            @JsonProperty("predavac") String predavac,
            @JsonProperty("ostaliPredavaci") List<String> ostaliPredavaci,
            @JsonProperty("predmet") String predmet,
            @JsonProperty("studentskeGrupe") Object studentskeGrupe, // Use Object to handle both String and List<String>
            @JsonProperty("biWeekly") boolean biWeekly) {
        this.id = id;
        this.tipProstorije = tipProstorije;
        this.meetingTip = meetingTip;
        this.brojCasova = brojCasova;
        this.predavac = predavac;
        this.ostaliPredavaci = ostaliPredavaci;
        this.predmet = predmet;

        // Handle studentskeGrupe
        this.studentskeGrupe = new ArrayList<>();
        if (studentskeGrupe instanceof String) {
            this.studentskeGrupe.add((String) studentskeGrupe);
        } else if (studentskeGrupe instanceof List) {
            this.studentskeGrupe.addAll((List<String>) studentskeGrupe);
        }

        this.biWeekly = biWeekly;
    }
}
