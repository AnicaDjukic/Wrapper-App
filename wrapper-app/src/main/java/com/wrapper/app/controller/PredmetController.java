package com.wrapper.app.controller;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.dto.PredmetRequestDto;
import com.wrapper.app.dto.PredmetResponseDto;
import com.wrapper.app.service.PredmetService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/predmeti")
public class PredmetController {

    private final PredmetService service;

    private final ModelMapper modelMapper;

    public PredmetController(PredmetService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PredmetResponseDto> getAll() {
        return modelMapper.map(service.getAll(), new TypeToken<ArrayList<PredmetResponseDto>>() {}.getType());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredmetResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), PredmetResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PredmetResponseDto create(@RequestBody PredmetRequestDto dto) {
        Predmet saved =  service.create(modelMapper.map(dto, Predmet.class));
        return modelMapper.map(saved, PredmetResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public PredmetResponseDto update(@PathVariable String  id, @RequestBody PredmetRequestDto dto) {
        Predmet updated = service.update(id, modelMapper.map(dto, Predmet.class));
        return modelMapper.map(updated, PredmetResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredmetResponseDto delete(@PathVariable String id) {
        return modelMapper.map(service.deleteById(id), PredmetResponseDto.class);
    }

}
