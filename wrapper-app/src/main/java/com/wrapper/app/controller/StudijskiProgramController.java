package com.wrapper.app.controller;

import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.dto.StudijskiProgramRequestDto;
import com.wrapper.app.dto.StudijskiProgramResponseDto;
import com.wrapper.app.dto.StudijskiProgramSearchDto;
import com.wrapper.app.service.StudijskiProgramService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/studijski-programi")
public class StudijskiProgramController {

    private final StudijskiProgramService service;

    private final ModelMapper modelMapper;

    public StudijskiProgramController(StudijskiProgramService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudijskiProgramResponseDto> getAll() {
        return service.getAll().stream().map(sp -> modelMapper.map(sp, StudijskiProgramResponseDto.class)).toList();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public List<StudijskiProgramResponseDto> search(@RequestParam String oznaka,
                                                    @RequestParam String naziv,
                                                    @RequestParam String stepenStudija) {
        StudijskiProgramSearchDto searchDto = new StudijskiProgramSearchDto(Pattern.quote(oznaka.trim()),
                Pattern.quote(naziv.trim()), stepenStudija);
        return service.search(searchDto).stream().map(p -> modelMapper.map(p, StudijskiProgramResponseDto.class)).toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), StudijskiProgramResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public StudijskiProgramResponseDto update(@PathVariable String id, @RequestBody @Valid StudijskiProgramRequestDto dto) {
        StudijskiProgram updated = service.update(id, modelMapper.map(dto, StudijskiProgram.class));
        return modelMapper.map(updated, StudijskiProgramResponseDto.class);
    }
}
