package com.wrapper.app.domain;

import com.wrapper.app.dto.optimizator.MeetingAssignment;
import com.wrapper.app.dto.optimizator.Semestar;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeetingSchedule {

    private List<OrganizacionaJedinica> departmanList;

    private List<OrganizacionaJedinica> katedraList;

    private List<StudijskiProgram> studProgramList;

    private List<Predmet> predmetList;

    private List<Meeting> meetingList;

    private List<Dan> danList;

    private List<TimeGrain> timeGrainList;

    private List<Prostorija> prostorijaList;

    private List<Predavac> predavacList;

    private List<StudentskaGrupa> studentskaGrupaList;

    private List<MeetingAssignment> meetingAssignmentList;

    private Semestar semestar;

}
