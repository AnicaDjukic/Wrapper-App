package com.wrapper.app.controller;

import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.dto.request.StudentskaGrupaRequestDto;
import com.wrapper.app.dto.response.StudentskaGrupaResponseDto;
import com.wrapper.app.dto.search.StudentskaGrupaSearchDto;
import com.wrapper.app.dto.request.StudentskeGrupeRequestDto;
import com.wrapper.app.service.StudentskaGrupaService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/v1/studentske-grupe")
public class StudentskaGrupaController {

    private final StudentskaGrupaService service;

    private final ModelMapper modelMapper;

    public StudentskaGrupaController(StudentskaGrupaService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentskaGrupaResponseDto> getAll(@RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAll(PageRequest.of(page, size)).map(s -> modelMapper.map(s, StudentskaGrupaResponseDto.class));
    }

    @CrossOrigin(origins = "*")
    @GetMapping("search")
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentskaGrupaResponseDto> search(@RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam String oznaka,
                                              @RequestParam String godina,
                                              @RequestParam String brojStudenata,
                                              @RequestParam String studProg) {
        StudentskaGrupaSearchDto searchDto = new StudentskaGrupaSearchDto(Pattern.quote(oznaka.trim()), godina, brojStudenata, Pattern.quote(studProg.trim()));
        return service.search(searchDto, PageRequest.of(page, size)).map(s -> modelMapper.map(s, StudentskaGrupaResponseDto.class));
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentskaGrupaResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), StudentskaGrupaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<StudentskaGrupaResponseDto> create(@RequestBody StudentskeGrupeRequestDto dto) {
        List<StudentskaGrupa> saved =  service.create(dto);
        return saved.stream().map(s -> modelMapper.map(s, StudentskaGrupaResponseDto.class)).toList();
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{id}")
    public StudentskaGrupaResponseDto update(@PathVariable String id, @RequestBody @Valid StudentskaGrupaRequestDto dto) {
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
