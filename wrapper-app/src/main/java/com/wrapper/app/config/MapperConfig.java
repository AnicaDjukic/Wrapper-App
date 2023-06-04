package com.wrapper.app.config;

import com.wrapper.app.domain.*;
import com.wrapper.app.dto.*;
import com.wrapper.app.mapper.PredmetMapper;
import com.wrapper.app.mapper.PredmetPredavacMapper;
import com.wrapper.app.mapper.StudentskaGrupaMapper;
import com.wrapper.app.mapper.StudijskiProgramPredmetiMapper;
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

    PredmetMapper predmetMapper = new PredmetMapper();

    StudentskaGrupaMapper studentskaGrupaMapper = new StudentskaGrupaMapper();

    private final PredmetPredavacMapper predmetPredavacMapper;

    private final StudijskiProgramPredmetiMapper studijskiProgramPredmetiMapper;

    public MapperConfig(PredmetPredavacMapper predmetPredavacMapper, StudijskiProgramPredmetiMapper studijskiProgramPredmetiMapper) {
        this.predmetPredavacMapper = predmetPredavacMapper;
        this.studijskiProgramPredmetiMapper = studijskiProgramPredmetiMapper;
    }

    @Bean
    public ModelMapper modelMapper() {

        ModelMapper modelMapper = new ModelMapper();

        Converter<Predmet, PredmetResponseDto> predmetConverter = new AbstractConverter<>() {
            @Override
            protected PredmetResponseDto convert(Predmet predmet) {
                return predmetMapper.map(predmet);
            }
        };
        modelMapper.addConverter(predmetConverter);

        TypeMap<Predavac, PredavacResponseDto> predavacMapper = modelMapper.createTypeMap(Predavac.class, PredavacResponseDto.class);
        predavacMapper.addMappings(
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
        TypeMap<Prostorija, ProstorijaResponseDto> prostorijaMapper = modelMapper.createTypeMap(Prostorija.class, ProstorijaResponseDto.class);
        prostorijaMapper.addMappings(mapper -> mapper.using(orgJedinicaConverter).map(Prostorija::getOrgJedinica, ProstorijaResponseDto::setOrgJedinica));

        Converter<StudentskaGrupa, StudentskaGrupaResponseDto> studentskaGrupaConverter = new AbstractConverter<>() {
            @Override
            protected StudentskaGrupaResponseDto convert(StudentskaGrupa studentskaGrupa) {
                return studentskaGrupaMapper.map(studentskaGrupa);
            }
        };
        modelMapper.addConverter(studentskaGrupaConverter);

        Converter<StudijskiProgramPredmeti, StudijskiProgramPredmetiDto> studijskiProgramPredmetiConverter = new AbstractConverter<>() {
            @Override
            protected StudijskiProgramPredmetiDto convert(StudijskiProgramPredmeti studijskiProgramPredmeti) {
                return studijskiProgramPredmetiMapper.map(studijskiProgramPredmeti);
            }
        };
        modelMapper.addConverter(studijskiProgramPredmetiConverter);

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
