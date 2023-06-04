package com.wrapper.app.controller;

import com.wrapper.app.domain.PredmetPredavac;
import com.wrapper.app.dto.PredavaciDto;
import com.wrapper.app.dto.PredmetPredavaciDto;
import com.wrapper.app.dto.StudijskiProgramPredmetiDto;
import com.wrapper.app.service.StudijskiProgramPredmetiService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/studijski-programi")
public class StudijskiProgramPredmetiController {

    private final StudijskiProgramPredmetiService service;

    private final ModelMapper modelMapper;

    public StudijskiProgramPredmetiController(StudijskiProgramPredmetiService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("{studProgramId}/predmeti")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramPredmetiDto getByStudijskiProgramId(@PathVariable String studProgramId) {
        return modelMapper.map(service.getById(studProgramId), StudijskiProgramPredmetiDto.class);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("{studProgramId}/predmeti")
    public StudijskiProgramPredmetiDto addPredmet(@PathVariable String studProgramId, @RequestBody PredmetPredavaciDto dto) {
        PredmetPredavac predmetPredavac = modelMapper.map(dto, PredmetPredavac.class);
        return modelMapper.map(service.addPredmet(studProgramId, predmetPredavac), StudijskiProgramPredmetiDto.class);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("{studProgramId}/predmeti/{predmetId}")
    public StudijskiProgramPredmetiDto updatePredmet(@PathVariable String studProgramId, @PathVariable String predmetId, @RequestBody PredavaciDto dto) {
        PredmetPredavac predmetPredavac = modelMapper.map(dto, PredmetPredavac.class);
        return modelMapper.map(service.updatePredmet(studProgramId, predmetId, predmetPredavac), StudijskiProgramPredmetiDto.class);
    }

    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{studProgramId}/predmeti/{predmetId}")
    public void removePredmet(@PathVariable String studProgramId, @PathVariable String predmetId) {
        service.removePredmet(studProgramId, predmetId);
    }
}
