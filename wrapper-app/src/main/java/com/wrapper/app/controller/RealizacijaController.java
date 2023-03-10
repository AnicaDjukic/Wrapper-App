package com.wrapper.app.controller;

import com.wrapper.app.dto.RealizacijaRequestDto;
import com.wrapper.app.dto.RealizacijaResponseDto;
import com.wrapper.app.service.RealizacijaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/realizacije")
public class RealizacijaController {

    private final RealizacijaService service;

    private final ModelMapper modelMapper;

    public RealizacijaController(RealizacijaService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RealizacijaResponseDto> getAll() {
        return modelMapper.map(service.getAll(), new TypeToken<ArrayList<RealizacijaResponseDto>>() {}.getType());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public RealizacijaResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), RealizacijaResponseDto.class);
    }

    @PutMapping("{id}")
    public RealizacijaResponseDto update(@PathVariable String id, @RequestBody RealizacijaRequestDto dto) {
        return modelMapper.map(service.update(id, dto), RealizacijaResponseDto.class);
    }
}
