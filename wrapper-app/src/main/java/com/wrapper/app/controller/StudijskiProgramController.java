package com.wrapper.app.controller;

import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.dto.StudijskiProgramRequestDto;
import com.wrapper.app.dto.StudijskiProgramResponseDto;
import com.wrapper.app.service.StudijskiProgramService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/studijski-programi")
public class StudijskiProgramController {

    private final StudijskiProgramService service;

    private final ModelMapper modelMapper;

    public StudijskiProgramController(StudijskiProgramService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudijskiProgramResponseDto> getAll() {
        return modelMapper.map(service.getAll(), new TypeToken<ArrayList<StudijskiProgramResponseDto>>() {}.getType());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), StudijskiProgramResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramResponseDto create(@RequestBody StudijskiProgramRequestDto dto) {
        StudijskiProgram saved =  service.create(modelMapper.map(dto, StudijskiProgram.class));
        return modelMapper.map(saved, StudijskiProgramResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public StudijskiProgramResponseDto update(@PathVariable String  id, @RequestBody StudijskiProgramRequestDto dto) {
        StudijskiProgram updated = service.update(id, modelMapper.map(dto, StudijskiProgram.class));
        return modelMapper.map(updated, StudijskiProgramResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramResponseDto delete(@PathVariable String id) {
        return modelMapper.map(service.deleteById(id), StudijskiProgramResponseDto.class);
    }
}
