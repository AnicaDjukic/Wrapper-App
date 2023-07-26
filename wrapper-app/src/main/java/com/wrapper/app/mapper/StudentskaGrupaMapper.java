package com.wrapper.app.mapper;

import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.dto.generator.StudentskaGrupaDto;
import com.wrapper.app.dto.request.StudentskaGrupaRequestDto;
import com.wrapper.app.service.StudijskiProgramService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StudentskaGrupaMapper {

    private final StudijskiProgramService studijskiProgramService;

    public StudentskaGrupaMapper(StudijskiProgramService studijskiProgramService) {
        this.studijskiProgramService = studijskiProgramService;
    }

    public StudentskaGrupa map(StudentskaGrupaRequestDto dto) {
        return new StudentskaGrupa(
                UUID.randomUUID().toString(),
                dto.getOznaka(),
                dto.getGodina(),
                dto.getSemestar(),
                dto.getBrojStudenata(),
                studijskiProgramService.getById(dto.getStudijskiProgramId()));
    }

    public StudentskaGrupaDto map(StudentskaGrupa studentskaGrupa) {
        return new StudentskaGrupaDto(
                studentskaGrupa.getId(),
                studentskaGrupa.getOznaka(),
                studentskaGrupa.getGodina(),
                studentskaGrupa.getSemestar(),
                studentskaGrupa.getBrojStudenata(),
                studentskaGrupa.getStudijskiProgram().getId());
    }
}
