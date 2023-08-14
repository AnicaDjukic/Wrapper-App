package com.wrapper.app.presentation.controller;

import com.wrapper.app.domain.model.Predavac;
import com.wrapper.app.presentation.dto.request.PredavacRequestDto;
import com.wrapper.app.presentation.dto.response.PredavacResponseDto;
import com.wrapper.app.presentation.dto.search.PredavacSearchDto;
import com.wrapper.app.application.service.PredavacService;
import jakarta.validation.Valid;
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

    public PredavacController(PredavacService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PredavacResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAll(PageRequest.of(page, size)).map(p -> modelMapper.map(p, PredavacResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public Page<PredavacResponseDto> search(@RequestParam(required = false, defaultValue = "0") int page,
                                            @RequestParam(required = false, defaultValue = "10") int size,
                                            @RequestParam String oznaka,
                                            @RequestParam String ime,
                                            @RequestParam String prezime,
                                            @RequestParam String orgJed) {
        PredavacSearchDto searchDto = new PredavacSearchDto(oznaka.trim(), ime.trim(), prezime.trim(), orgJed.trim());
        return service.search(searchDto, PageRequest.of(page, size)).map(p -> modelMapper.map(p, PredavacResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public PredavacResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), PredavacResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PredavacResponseDto create(@RequestBody @Valid PredavacRequestDto dto) {
        Predavac saved =  service.create(modelMapper.map(dto, Predavac.class));
        return modelMapper.map(saved, PredavacResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public PredavacResponseDto update(@PathVariable String id, @RequestBody @Valid PredavacRequestDto dto) {
        Predavac updated = service.update(id, modelMapper.map(dto, Predavac.class));
        return modelMapper.map(updated, PredavacResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
       service.deleteById(id);
    }
}
