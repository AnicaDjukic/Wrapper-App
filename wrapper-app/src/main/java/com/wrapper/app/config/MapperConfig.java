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

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {


    private final PredmetMapper predmetMapper;

    private final PredavacMapper predavacMapper;

    StudentskaGrupaMapper studentskaGrupaMapper = new StudentskaGrupaMapper();

    private final PredmetPredavacMapper predmetPredavacMapper;

    private final StudijskiProgramMapper studijskiProgramMapper;

    private final ProstorijaMapper prostorijaMapper;

    public MapperConfig(PredmetMapper predmetMapper, PredavacMapper predavacMapper, PredmetPredavacMapper predmetPredavacMapper, StudijskiProgramMapper studijskiProgramMapper, ProstorijaMapper prostorijaMapper) {
        this.predmetMapper = predmetMapper;
        this.predavacMapper = predavacMapper;
        this.predmetPredavacMapper = predmetPredavacMapper;
        this.studijskiProgramMapper = studijskiProgramMapper;
        this.prostorijaMapper = prostorijaMapper;
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        Converter<PredmetRequestDto, Predmet> predmetConverter = new AbstractConverter<>() {
            @Override
            protected Predmet convert(PredmetRequestDto predmetRequestDto) {
                return predmetMapper.map(predmetRequestDto);
            }
        };
        modelMapper.addConverter(predmetConverter);

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

        Converter<List<OrganizacionaJedinica>, List<String>> orgJedinicaConverter = ctx -> {
            List<OrganizacionaJedinica> orgJedinicaList = ctx.getSource();
            if (orgJedinicaList != null) {
                return orgJedinicaList.stream()
                        .map(OrganizacionaJedinica::getNaziv)
                        .collect(Collectors.toList());
            }
            return null;
        };
        modelMapper.addConverter(orgJedinicaConverter);

        TypeMap<Prostorija, ProstorijaResponseDto> prostorijaResponseConverter = modelMapper.createTypeMap(Prostorija.class, ProstorijaResponseDto.class);
        prostorijaResponseConverter.addMappings(mapper -> mapper.using(orgJedinicaConverter).map(Prostorija::getOrgJedinica, ProstorijaResponseDto::setOrgJedinica));

        Converter<ProstorijaRequestDto, Prostorija> prostorijaRequestConverter = new AbstractConverter<>() {
            @Override
            protected Prostorija convert(ProstorijaRequestDto prostorijaRequestDto) {
                return prostorijaMapper.map(prostorijaRequestDto);
            }
        };
        modelMapper.addConverter(prostorijaRequestConverter);

        Converter<StudentskaGrupa, StudentskaGrupaResponseDto> studentskaGrupaConverter = new AbstractConverter<>() {
            @Override
            protected StudentskaGrupaResponseDto convert(StudentskaGrupa studentskaGrupa) {
                return studentskaGrupaMapper.map(studentskaGrupa);
            }
        };
        modelMapper.addConverter(studentskaGrupaConverter);

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

        return modelMapper;
    }
}
