package com.wrapper.app.controller;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.dto.PredmetRequestDto;
import com.wrapper.app.dto.PredmetResponseDto;
import com.wrapper.app.dto.PredmetSearchDto;
import com.wrapper.app.service.PredmetService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<PredmetResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAll(PageRequest.of(page, size)).map(p -> modelMapper.map(p, PredmetResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public Page<PredmetResponseDto> search(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size,
                                           @RequestParam String oznaka,
                                           @RequestParam String naziv,
                                           @RequestParam String studProg) {
        PredmetSearchDto searchDto = new PredmetSearchDto(oznaka.trim(), naziv.trim(), studProg.trim());
        return service.search(searchDto, PageRequest.of(page, size)).map(p -> modelMapper.map(p, PredmetResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredmetResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), PredmetResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/studijski-program/{studijskiProgram}")
    @ResponseStatus(HttpStatus.OK)
    public List<PredmetResponseDto> getByStudijskiProgram(@PathVariable String studijskiProgram) {
        return modelMapper.map(service.getByStudijskiProgram(studijskiProgram), new TypeToken<ArrayList<PredmetResponseDto>>() {}.getType());
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PredmetResponseDto create(@RequestBody PredmetRequestDto dto) {
        Predmet saved = service.create(modelMapper.map(dto, Predmet.class));
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
