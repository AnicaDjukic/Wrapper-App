package com.wrapper.app.presentation.controller;

import com.wrapper.app.presentation.dto.response.OrganizacionaJedinicaDto;
import com.wrapper.app.application.service.OrganizacionaJedinicaService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/organizacione-jedinice")
public class OrganizacionaJedinicaController {

    private final OrganizacionaJedinicaService service;

    private final ModelMapper modelMapper;

    public OrganizacionaJedinicaController(OrganizacionaJedinicaService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrganizacionaJedinicaDto> getAll() {
        return service.getAll().stream().map(o -> modelMapper.map(o, OrganizacionaJedinicaDto.class)).toList();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrganizacionaJedinicaDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), OrganizacionaJedinicaDto.class);
    }
}
