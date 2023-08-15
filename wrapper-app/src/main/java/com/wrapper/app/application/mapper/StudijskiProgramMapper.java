package com.wrapper.app.application.mapper;

import com.wrapper.app.application.util.StudyTypes;
import com.wrapper.app.domain.model.StudijskiProgram;
import com.wrapper.app.presentation.dto.response.StudijskiProgramDto;
import com.wrapper.app.presentation.dto.response.StudijskiProgramResponseDto;
import org.springframework.stereotype.Component;

@Component
public class StudijskiProgramMapper {

    public StudijskiProgramResponseDto mapToResponseDto(StudijskiProgram studijskiProgram) {
        StudijskiProgramResponseDto dto = new StudijskiProgramResponseDto();
        dto.setId(studijskiProgram.getId());
        dto.setOznaka(studijskiProgram.getOznaka());
        dto.setNaziv(studijskiProgram.getNaziv());
        dto.setStepen(studijskiProgram.getStepen());
        dto.setNivo(studijskiProgram.getNivo());
        dto.setStepenStudija(getStepenStudija(studijskiProgram.getStepen(), studijskiProgram.getNivo()));
        dto.setBlock(studijskiProgram.isBlock());
        return dto;
    }

    public StudijskiProgramDto mapToDto(StudijskiProgram studijskiProgram) {
        StudijskiProgramDto dto = new StudijskiProgramDto();
        dto.setOznaka(studijskiProgram.getOznaka());
        dto.setNaziv(studijskiProgram.getNaziv());
        dto.setStepenStudija(getStepenStudija(studijskiProgram.getStepen(), studijskiProgram.getNivo()));
        return dto;
    }

    public String getStepenStudija(int stepen, int nivo) {
        return switch (stepen) {
            case 1 -> (nivo == 1) ? StudyTypes.OAS : StudyTypes.OSS;
            case 2 -> (nivo == 1) ? StudyTypes.MAS : StudyTypes.MSS;
            default -> "";
        };
    }

}
