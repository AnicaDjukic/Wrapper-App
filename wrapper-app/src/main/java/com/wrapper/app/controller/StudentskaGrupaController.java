package com.wrapper.app.controller;

import com.wrapper.app.domain.StudentskaGrupa;
import com.wrapper.app.dto.StudentskaGrupaRequestDto;
import com.wrapper.app.dto.StudentskaGrupaResponseDto;
import com.wrapper.app.dto.StudentskaGrupaSearchDto;
import com.wrapper.app.service.StudentskaGrupaService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        StudentskaGrupaSearchDto searchDto = new StudentskaGrupaSearchDto(oznaka.trim(), godina, brojStudenata, studProg.trim());
        return service.search(searchDto, PageRequest.of(page, size)).map(p -> modelMapper.map(p, StudentskaGrupaResponseDto.class));
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
