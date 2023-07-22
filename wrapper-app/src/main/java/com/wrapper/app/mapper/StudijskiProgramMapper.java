package com.wrapper.app.mapper;

import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.dto.response.StudijskiProgramDto;
import com.wrapper.app.dto.response.StudijskiProgramResponseDto;
import org.springframework.stereotype.Component;

@Component
public class StudijskiProgramMapper {

    private static final String OAS = "OSNOVNE AKADEMSKE STUDIJE";
    private static final String OSS = "OSNOVNE STRUKOVNE STUDIJE";
    private static final String MAS = "MASTER AKADEMSKE STUDIJE";
    private static final String MSS = "MASTER STRUKOVNE STUDIJE";

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
            case 1 -> (nivo == 1) ? OAS : OSS;
            case 2 -> (nivo == 1) ? MAS : MSS;
            default -> "";
        };
    }

}
