package com.wrapper.app.config;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.generator.PredmetDto;
import com.wrapper.app.dto.generator.ProstorijaDto;
import com.wrapper.app.dto.generator.StudentskaGrupaDto;
import com.wrapper.app.dto.generator.MeetingDto;
import com.wrapper.app.dto.request.*;
import com.wrapper.app.dto.response.PredavacResponseDto;
import com.wrapper.app.dto.response.StudijskiProgramDto;
import com.wrapper.app.dto.response.StudijskiProgramResponseDto;
import com.wrapper.app.mapper.*;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    private final PredmetMapper predmetMapper;

    private final PredavacMapper predavacMapper;

    private final PredmetPredavacMapper predmetPredavacMapper;

    private final StudijskiProgramMapper studijskiProgramMapper;

    private final ProstorijaMapper prostorijaMapper;

    private final StudentskaGrupaMapper studentskaGrupaMapper;

    private final StudijskiProgramPredmetiMapper studijskiProgramPredmetiMapper;

    private final MeetingMapper meetingMapper;

    public MapperConfig(PredmetMapper predmetMapper,
                        PredavacMapper predavacMapper,
                        PredmetPredavacMapper predmetPredavacMapper,
                        StudijskiProgramMapper studijskiProgramMapper,
                        ProstorijaMapper prostorijaMapper,
                        StudentskaGrupaMapper studentskaGrupaMapper,
                        StudijskiProgramPredmetiMapper studijskiProgramPredmetiMapper,
                        MeetingMapper meetingMapper) {
        this.predmetMapper = predmetMapper;
        this.predavacMapper = predavacMapper;
        this.predmetPredavacMapper = predmetPredavacMapper;
        this.studijskiProgramMapper = studijskiProgramMapper;
        this.prostorijaMapper = prostorijaMapper;
        this.studentskaGrupaMapper = studentskaGrupaMapper;
        this.studijskiProgramPredmetiMapper = studijskiProgramPredmetiMapper;
        this.meetingMapper = meetingMapper;
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        initilizePredmetConverters(modelMapper);
        initilizePredavacConverters(modelMapper);
        initilizeProstorijaConverters(modelMapper);
        initilizeStudijskiProgramConverters(modelMapper);
        initilizePredmetPredavacConverters(modelMapper);
        initilizeStudentskaGrupaConverters(modelMapper);
        initilizeStudijskiProgramPredmetiConverters(modelMapper);
        initilizeMeetingConverters(modelMapper);
        return modelMapper;
    }

    private void initilizePredmetConverters(ModelMapper modelMapper) {
        Converter<PredmetRequestDto, Predmet> predmetConverter = new AbstractConverter<>() {
            @Override
            protected Predmet convert(PredmetRequestDto predmetRequestDto) {
                return predmetMapper.map(predmetRequestDto);
            }
        };
        modelMapper.addConverter(predmetConverter);

        Converter<Predmet, PredmetDto> predmetDtoConverter = new AbstractConverter<>() {
            @Override
            protected PredmetDto convert(Predmet predmet) {
                return predmetMapper.map(predmet);
            }
        };
        modelMapper.addConverter(predmetDtoConverter);

        Converter<Predmet, com.wrapper.app.dto.converter.PredmetDto> converterPredmetDtoConverter = new AbstractConverter<>() {
            @Override
            protected com.wrapper.app.dto.converter.PredmetDto convert(Predmet predmet) {
                return predmetMapper.mapToConverterDto(predmet);
            }
        };
        modelMapper.addConverter(converterPredmetDtoConverter);
    }

    private void initilizePredavacConverters(ModelMapper modelMapper) {
        Converter<PredavacRequestDto, Predavac> predavacRequestConverter =  new AbstractConverter<>() {
            @Override
            protected Predavac convert(PredavacRequestDto predavacRequestDto) {
                return predavacMapper.map(predavacRequestDto);
            }
        };
        modelMapper.addConverter(predavacRequestConverter);

        TypeMap<Predavac, PredavacResponseDto> predavacConverter = modelMapper.createTypeMap(Predavac.class, PredavacResponseDto.class);
        predavacConverter.addMappings(
                mapper -> mapper.map(src -> src.getOrgJedinica().getNaziv(), PredavacResponseDto::setOrgJedinica)
        );

        Converter<Predavac, com.wrapper.app.dto.generator.PredavacDto> predavacDtoConverter =  new AbstractConverter<>() {
            @Override
            protected com.wrapper.app.dto.generator.PredavacDto convert(Predavac predavac) {
                return predavacMapper.map(predavac);
            }
        };
        modelMapper.addConverter(predavacDtoConverter);

        Converter<Predavac, com.wrapper.app.dto.converter.PredavacDto> converterPredavacDtoConverter =  new AbstractConverter<>() {
            @Override
            protected com.wrapper.app.dto.converter.PredavacDto convert(Predavac predavac) {
                return predavacMapper.mapToConverterDto(predavac);
            }
        };
        modelMapper.addConverter(converterPredavacDtoConverter);
    }

    private void initilizeProstorijaConverters(ModelMapper modelMapper) {
        Converter<ProstorijaRequestDto, Prostorija> prostorijaRequestConverter = new AbstractConverter<>() {
            @Override
            protected Prostorija convert(ProstorijaRequestDto prostorijaRequestDto) {
                return prostorijaMapper.map(prostorijaRequestDto);
            }
        };
        modelMapper.addConverter(prostorijaRequestConverter);

        Converter<Prostorija, ProstorijaDto> prostorijaDtoConverter = new AbstractConverter<Prostorija, ProstorijaDto>() {
            @Override
            protected ProstorijaDto convert(Prostorija prostorija) {
                return prostorijaMapper.map(prostorija);
            }
        };
        modelMapper.addConverter(prostorijaDtoConverter);
    }

    private void initilizeStudijskiProgramConverters(ModelMapper modelMapper) {
        Converter<StudijskiProgram, StudijskiProgramResponseDto> studijskiProgramResponseConverter = new AbstractConverter<>() {
            @Override
            protected StudijskiProgramResponseDto convert(StudijskiProgram studijskiProgram) {
                return studijskiProgramMapper.mapToResponseDto(studijskiProgram);
            }
        };
        modelMapper.addConverter(studijskiProgramResponseConverter);

        Converter<StudijskiProgram, StudijskiProgramDto> studijskiProgramConverter = new AbstractConverter<>() {
            @Override
            protected StudijskiProgramDto convert(StudijskiProgram studijskiProgram) {
                return studijskiProgramMapper.mapToDto(studijskiProgram);
            }
        };
        modelMapper.addConverter(studijskiProgramConverter);
    }

    private void initilizePredmetPredavacConverters(ModelMapper modelMapper) {
        Converter<PredmetPredavaciDto, PredmetPredavac> predmetPredavacConverter = new AbstractConverter<>() {
            @Override
            protected PredmetPredavac convert(PredmetPredavaciDto predmetPredavaciDto) {
                return predmetPredavacMapper.map(predmetPredavaciDto);
            }
        };
        modelMapper.addConverter(predmetPredavacConverter);

        Converter<PredavaciDto, PredmetPredavac> predavaciConverter = new AbstractConverter<>() {
            @Override
            protected PredmetPredavac convert(PredavaciDto predavaciDto) {
                return predmetPredavacMapper.map(predavaciDto);
            }
        };
        modelMapper.addConverter(predavaciConverter);
    }

    private void initilizeStudentskaGrupaConverters(ModelMapper modelMapper) {
        Converter<StudentskaGrupaRequestDto, StudentskaGrupa> studentskaGrupaConverter = new AbstractConverter<>() {
            @Override
            protected StudentskaGrupa convert(StudentskaGrupaRequestDto studentskaGrupaRequestDto) {
                return studentskaGrupaMapper.map(studentskaGrupaRequestDto);
            }
        };
        modelMapper.addConverter(studentskaGrupaConverter);

        Converter<StudentskaGrupa, StudentskaGrupaDto> studentskaGrupaDtoConverter = new AbstractConverter<>() {
            @Override
            protected StudentskaGrupaDto convert(StudentskaGrupa studentskaGrupa) {
                return studentskaGrupaMapper.map(studentskaGrupa);
            }
        };
        modelMapper.addConverter(studentskaGrupaDtoConverter);
    }

    private void initilizeStudijskiProgramPredmetiConverters(ModelMapper modelMapper) {
        Converter<StudijskiProgramPredmeti, com.wrapper.app.dto.generator.StudijskiProgramPredmetiDto> converter = new AbstractConverter<>() {
            @Override
            protected com.wrapper.app.dto.generator.StudijskiProgramPredmetiDto convert(StudijskiProgramPredmeti studijskiProgramPredmeti) {
                return studijskiProgramPredmetiMapper.map(studijskiProgramPredmeti);
            }
        };
        modelMapper.addConverter(converter);
    }

    private void initilizeMeetingConverters(ModelMapper modelMapper) {
        Converter<MeetingDto, Meeting> meetingConverter = new AbstractConverter<>() {
            @Override
            protected Meeting convert(MeetingDto meetingDto) {
                return meetingMapper.map(meetingDto);
            }
        };
        modelMapper.addConverter(meetingConverter);
    }

}
