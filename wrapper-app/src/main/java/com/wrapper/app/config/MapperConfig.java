package com.wrapper.app.config;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.*;
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

    public MapperConfig(PredmetMapper predmetMapper,
                        PredavacMapper predavacMapper,
                        PredmetPredavacMapper predmetPredavacMapper,
                        StudijskiProgramMapper studijskiProgramMapper,
                        ProstorijaMapper prostorijaMapper,
                        StudentskaGrupaMapper studentskaGrupaMapper) {
        this.predmetMapper = predmetMapper;
        this.predavacMapper = predavacMapper;
        this.predmetPredavacMapper = predmetPredavacMapper;
        this.studijskiProgramMapper = studijskiProgramMapper;
        this.prostorijaMapper = prostorijaMapper;
        this.studentskaGrupaMapper = studentskaGrupaMapper;
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
    }

    private void initilizePredavacConverters(ModelMapper modelMapper) {
        Converter<PredavacRequestDto, Predavac> predavacRequstConverter =  new AbstractConverter<>() {
            @Override
            protected Predavac convert(PredavacRequestDto predavacRequestDto) {
                return predavacMapper.map(predavacRequestDto);
            }
        };
        modelMapper.addConverter(predavacRequstConverter);

        TypeMap<Predavac, PredavacResponseDto> predavacConverter = modelMapper.createTypeMap(Predavac.class, PredavacResponseDto.class);
        predavacConverter.addMappings(
                mapper -> mapper.map(src -> src.getOrgJedinica().getNaziv(), PredavacResponseDto::setOrgJedinica)
        );
    }

    private void initilizeProstorijaConverters(ModelMapper modelMapper) {
        Converter<ProstorijaRequestDto, Prostorija> prostorijaRequestConverter = new AbstractConverter<>() {
            @Override
            protected Prostorija convert(ProstorijaRequestDto prostorijaRequestDto) {
                return prostorijaMapper.map(prostorijaRequestDto);
            }
        };
        modelMapper.addConverter(prostorijaRequestConverter);
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
    }

}
