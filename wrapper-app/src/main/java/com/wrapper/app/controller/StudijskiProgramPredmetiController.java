package com.wrapper.app.controller;

import com.wrapper.app.domain.PredmetPredavac;
import com.wrapper.app.domain.StudijskiProgram;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import com.wrapper.app.dto.*;
import com.wrapper.app.service.StudijskiProgramPredmetiService;
import com.wrapper.app.service.event.StudijskiProgramDeletedEvent;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/realizacija/studijski-programi")
public class StudijskiProgramPredmetiController {

    private final StudijskiProgramPredmetiService service;

    private final ModelMapper modelMapper;

    private final ApplicationEventPublisher eventPublisher;

    public StudijskiProgramPredmetiController(StudijskiProgramPredmetiService service,
                                              ModelMapper modelMapper, ApplicationEventPublisher eventPublisher) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("{studProgramId}/predmeti")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramPredmetiDto getByStudijskiProgramId(@PathVariable String studProgramId) {
        return modelMapper.map(service.getById(studProgramId), StudijskiProgramPredmetiDto.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudijskiProgramPredmetiDto create(@RequestBody StudijskiProgramRequestDto dto) {
        StudijskiProgramPredmeti saved =  service.add(modelMapper.map(dto, StudijskiProgram.class));
        return modelMapper.map(saved, StudijskiProgramPredmetiDto.class);
    }

    @PostMapping("{studProgramId}/predmeti")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramPredmetiDto addPredmet(@PathVariable String studProgramId, @RequestBody PredmetPredavaciDto dto) {
        PredmetPredavac predmetPredavac = modelMapper.map(dto, PredmetPredavac.class);
        return modelMapper.map(service.addPredmet(studProgramId, predmetPredavac), StudijskiProgramPredmetiDto.class);
    }

    @PutMapping("{studProgramId}/predmeti/{predmetId}")
    @ResponseStatus(HttpStatus.OK)
    public StudijskiProgramPredmetiDto updatePredmet(@PathVariable String studProgramId, @PathVariable String predmetId, @RequestBody PredavaciDto dto) {
        PredmetPredavac predmetPredavac = modelMapper.map(dto, PredmetPredavac.class);
        return modelMapper.map(service.updatePredmet(studProgramId, predmetId, predmetPredavac), StudijskiProgramPredmetiDto.class);
    }

    @DeleteMapping("{studProgramId}/predmeti/{predmetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePredmet(@PathVariable String studProgramId, @PathVariable String predmetId) {
        service.removePredmet(studProgramId, predmetId);
    }

    @DeleteMapping("{studProgramId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String studProgramId) {
        StudijskiProgramDeletedEvent event = new StudijskiProgramDeletedEvent(studProgramId);
        eventPublisher.publishEvent(event);
        service.deleteById(studProgramId);
    }
}
