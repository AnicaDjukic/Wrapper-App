package com.wrapper.app.controller;

import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.dto.StudentskaGrupaRequestDto;
import com.wrapper.app.dto.StudentskaGrupaResponseDto;
import com.wrapper.app.service.StudentskaGrupaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/studentske-grupe")
public class StudentskaGrupaController {

    private final StudentskaGrupaService service;

    private final ModelMapper modelMapper;

    public StudentskaGrupaController(StudentskaGrupaService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudentskaGrupaResponseDto> getAll() {
        return modelMapper.map(service.getAll(), new TypeToken<ArrayList<StudentskaGrupaResponseDto>>(){}.getType());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentskaGrupaResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), StudentskaGrupaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public StudentskaGrupaResponseDto create(@RequestBody StudentskaGrupaRequestDto dto) {
        StudentskaGrupa saved =  service.create(modelMapper.map(dto, StudentskaGrupa.class));
        return modelMapper.map(saved, StudentskaGrupaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public StudentskaGrupaResponseDto update(@PathVariable String  id, @RequestBody StudentskaGrupaRequestDto dto) {
        StudentskaGrupa updated = service.update(id, modelMapper.map(dto, StudentskaGrupa.class));
        return modelMapper.map(updated, StudentskaGrupaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentskaGrupaResponseDto delete(@PathVariable String id) {
        return modelMapper.map(service.deleteById(id), StudentskaGrupaResponseDto.class);
    }
}
