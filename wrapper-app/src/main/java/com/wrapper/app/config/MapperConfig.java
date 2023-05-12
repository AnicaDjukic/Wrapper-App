package com.wrapper.app.config;

import com.wrapper.app.domain.Predavac;
import com.wrapper.app.domain.Predmet;
import com.wrapper.app.dto.PredavacResponseDto;
import com.wrapper.app.dto.PredmetRequestDto;
import com.wrapper.app.dto.PredmetResponseDto;
import com.wrapper.app.mapper.PredmetMapper;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

@Configuration
public class MapperConfig {

    PredmetMapper predmetMapper = new PredmetMapper();

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        Converter<Predmet, PredmetResponseDto> predmetResponseDtoConverter = new AbstractConverter<>() {
            @Override
            protected PredmetResponseDto convert(Predmet predmet) {
                return predmetMapper.map(predmet);
            }
        };

        modelMapper.addConverter(predmetResponseDtoConverter);

        TypeMap<Predavac, PredavacResponseDto> propertyMapper = modelMapper.createTypeMap(Predavac.class, PredavacResponseDto.class);
        propertyMapper.addMappings(
                mapper -> mapper.map(src -> src.getOrgJedinica().getNaziv(), PredavacResponseDto::setOrgJedinica)
        );

        return modelMapper;
    }
}
