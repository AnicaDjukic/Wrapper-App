package com.wrapper.app.controller;

import com.wrapper.app.domain.Predavac;
import com.wrapper.app.dto.PredavacRequestDto;
import com.wrapper.app.dto.PredavacResponseDto;
import com.wrapper.app.service.PredavacService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/predavaci")
public class PredavacController {

    private final PredavacService service;

    private final ModelMapper modelMapper;

    public PredavacController(PredavacService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PredavacResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAll(PageRequest.of(page, size)).map(p -> modelMapper.map(p, PredavacResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredavacResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), PredavacResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PredavacResponseDto create(@RequestBody PredavacRequestDto dto) {
        Predavac saved =  service.create(modelMapper.map(dto, Predavac.class));
        return modelMapper.map(saved, PredavacResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public PredavacResponseDto update(@PathVariable String  id, @RequestBody PredavacRequestDto dto) {
        Predavac updated = service.update(id, modelMapper.map(dto, Predavac.class));
        return modelMapper.map(updated, PredavacResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredavacResponseDto delete(@PathVariable String id) {
        return modelMapper.map(service.deleteById(id), PredavacResponseDto.class);
    }
}
