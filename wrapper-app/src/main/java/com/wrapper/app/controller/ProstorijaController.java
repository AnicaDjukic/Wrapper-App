package com.wrapper.app.controller;

import com.wrapper.app.domain.Prostorija;
import com.wrapper.app.dto.ProstorijaRequestDto;
import com.wrapper.app.dto.ProstorijaResponseDto;
import com.wrapper.app.service.ProstorijaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/prostorije")
public class ProstorijaController {

    private final ProstorijaService service;

    private final ModelMapper modelMapper;

    public ProstorijaController(ProstorijaService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProstorijaResponseDto> getAll() {
        return modelMapper.map(service.getAll(), new TypeToken<ArrayList<ProstorijaResponseDto>>(){}.getType());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProstorijaResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), ProstorijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProstorijaResponseDto create(@RequestBody ProstorijaRequestDto dto) {
        Prostorija saved =  service.create(modelMapper.map(dto, Prostorija.class));
        return modelMapper.map(saved, ProstorijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public ProstorijaResponseDto update(@PathVariable String  id, @RequestBody ProstorijaRequestDto dto) {
        Prostorija updated = service.update(id, modelMapper.map(dto, Prostorija.class));
        return modelMapper.map(updated, ProstorijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProstorijaResponseDto delete(@PathVariable String id) {
        return modelMapper.map(service.deleteById(id), ProstorijaResponseDto.class);
    }
}
