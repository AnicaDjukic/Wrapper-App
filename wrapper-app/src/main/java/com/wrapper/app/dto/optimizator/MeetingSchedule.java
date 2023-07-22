package com.wrapper.app.dto.optimizator;

import com.wrapper.app.domain.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeetingSchedule {

    private List<StudijskiProgram> studProgramList;

    private List<Meeting> meetingList;

    private List<Dan> danList;

    private List<TimeGrain> timeGrainList;

    private List<Prostorija> prostorijaList;

    private List<Predavac> predavacList;

    private List<StudentskaGrupa> studentskaGrupaList;

    private Semestar semestar;

}
