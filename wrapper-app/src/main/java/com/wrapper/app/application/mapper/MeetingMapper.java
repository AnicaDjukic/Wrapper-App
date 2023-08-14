package com.wrapper.app.application.mapper;

import com.wrapper.app.infrastructure.dto.generator.MeetingDto;
import com.wrapper.app.application.service.PredavacService;
import com.wrapper.app.application.service.PredmetService;
import com.wrapper.app.application.service.StudentskaGrupaService;
import com.wrapper.app.domain.model.Meeting;
import com.wrapper.app.domain.model.Predavac;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MeetingMapper {

    private final PredavacService predavacService;

    private final PredmetService predmetService;

    private final StudentskaGrupaService studentskaGrupaService;

    public MeetingMapper(PredavacService predavacService, PredmetService predmetService, StudentskaGrupaService studentskaGrupaService) {
        this.predavacService = predavacService;
        this.predmetService = predmetService;
        this.studentskaGrupaService = studentskaGrupaService;
    }

    public Meeting map(MeetingDto meetingDto) {
        return new Meeting(
                meetingDto.getId(),
                meetingDto.getTipProstorije(),
                meetingDto.getMeetingTip(),
                meetingDto.getBrojCasova(),
                predavacService.getById(meetingDto.getPredavac()),
                getOstaliProfesori(meetingDto),
                predmetService.getById(meetingDto.getPredmet()), meetingDto.getStudentskeGrupe().stream().map(studentskaGrupaService::getById).toList(),
                meetingDto.isBiWeekly()
        );
    }

    private List<Predavac> getOstaliProfesori(MeetingDto meetingDto) {
        if(meetingDto.getOstaliPredavaci().size() == 1 && meetingDto.getOstaliPredavaci().get(0).isEmpty())
            return new ArrayList<>();
        return meetingDto.getOstaliPredavaci().stream().map(predavacService::getById).toList();
    }


}
