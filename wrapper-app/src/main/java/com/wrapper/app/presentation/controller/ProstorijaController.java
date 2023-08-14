package com.wrapper.app.presentation.controller;

import com.wrapper.app.domain.model.Prostorija;
import com.wrapper.app.presentation.dto.request.ProstorijaRequestDto;
import com.wrapper.app.presentation.dto.response.ProstorijaResponseDto;
import com.wrapper.app.presentation.dto.search.ProstorijaSearchDto;
import com.wrapper.app.application.service.ProstorijaService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/prostorije")
public class ProstorijaController {

    private final ProstorijaService service;

    private final ModelMapper modelMapper;

    public ProstorijaController(ProstorijaService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProstorijaResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAll(PageRequest.of(page, size)).map(p -> modelMapper.map(p, ProstorijaResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProstorijaResponseDto> search(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size,
                                           @RequestParam String oznaka,
                                           @RequestParam String tip,
                                           @RequestParam String kapacitet,
                                           @RequestParam String orgJed) {
        ProstorijaSearchDto searchDto = new ProstorijaSearchDto(oznaka.trim(), tip, kapacitet, orgJed.trim());
        return service.search(searchDto, PageRequest.of(page, size)).map(p -> modelMapper.map(p, ProstorijaResponseDto.class));
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProstorijaResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), ProstorijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProstorijaResponseDto create(@RequestBody @Valid ProstorijaRequestDto dto) {
        Prostorija saved =  service.add(modelMapper.map(dto, Prostorija.class));
        return modelMapper.map(saved, ProstorijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public ProstorijaResponseDto update(@PathVariable String id, @RequestBody @Valid ProstorijaRequestDto dto) {
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
