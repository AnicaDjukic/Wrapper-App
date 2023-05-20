package com.wrapper.app.config;

import com.wrapper.app.domain.OrganizacionaJedinica;
import com.wrapper.app.domain.Predavac;
import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.Prostorija;
import com.wrapper.app.dto.PredavacResponseDto;
import com.wrapper.app.dto.PredmetResponseDto;
import com.wrapper.app.dto.ProstorijaResponseDto;
import com.wrapper.app.mapper.PredmetMapper;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperConfig {

    PredmetMapper predmetMapper = new PredmetMapper();

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<Predmet, PredmetResponseDto> predmetResponseDtoConverter = new AbstractConverter<>() {
            @Override
            protected PredmetResponseDto convert(Predmet predmet) {
                return predmetMapper.map(predmet);
            }
        };

        modelMapper.addConverter(predmetResponseDtoConverter);

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

        return modelMapper;
    }
}
