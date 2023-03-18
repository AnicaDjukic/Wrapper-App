package com.wrapper.app.controller;

import com.wrapper.app.dto.RealizacijaRequestDto;
import com.wrapper.app.dto.RealizacijaResponseDto;
import com.wrapper.app.dto.StudijskiProgramPredmetiDto;
import com.wrapper.app.service.RealizacijaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/realizacije")
public class RealizacijaController {

    private final RealizacijaService service;

    private final ModelMapper modelMapper;

    public RealizacijaController(RealizacijaService service) {
        this.service = service;
        this.modelMapper = new ModelMapper();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RealizacijaResponseDto> getAll() {
        return modelMapper.map(service.getAll(), new TypeToken<ArrayList<RealizacijaResponseDto>>() {}.getType());
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public RealizacijaResponseDto getById(@PathVariable String id) {
        return modelMapper.map(service.getById(id), RealizacijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("studijski-programi/{studProgramId}/predmeti")
    public RealizacijaResponseDto addPredmet(@PathVariable String studProgramId, @RequestBody RealizacijaRequestDto dto) {
        return modelMapper.map(service.addPredmet(studProgramId, dto), RealizacijaResponseDto.class);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("studijski-programi/{studProgramId}")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramPredmetiDto getByStudijskiProgramId(@PathVariable String studProgramId) {
        return service.getStudijskiProgramById(studProgramId);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("studijski-programi/{studProgramId}/predmeti/{predmetId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePredmetFromStudijskiProgram(@PathVariable String studProgramId, @PathVariable String predmetId) {
        service.deletePredmetInStudijskiProgram(studProgramId, predmetId);
    }
}
