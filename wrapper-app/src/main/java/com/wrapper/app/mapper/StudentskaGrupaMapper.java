package com.wrapper.app.mapper;

import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.dto.StudentskaGrupaResponseDto;

public class StudentskaGrupaMapper {

    public StudentskaGrupaResponseDto map(StudentskaGrupa studentskaGrupa) {
        StudentskaGrupaResponseDto responseDto = new StudentskaGrupaResponseDto();
        responseDto.setId(studentskaGrupa.getId());
        responseDto.setOznaka(studentskaGrupa.getOznaka());
        responseDto.setGodina(studentskaGrupa.getGodina());
        responseDto.setSemestar(studentskaGrupa.getSemestar());
        responseDto.setBrojStudenata(studentskaGrupa.getBrojStudenata());
        responseDto.setStudijskiProgram(studentskaGrupa.getStudijskiProgram().getOznaka()
                + " " + studentskaGrupa.getStudijskiProgram().getNaziv());
        return responseDto;
    }
}
