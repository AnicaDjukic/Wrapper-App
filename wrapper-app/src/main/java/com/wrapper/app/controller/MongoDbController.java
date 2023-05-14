package com.wrapper.app.controller;

import com.wrapper.app.domain.Database;
import com.wrapper.app.dto.DatabaseRequestDto;
import com.wrapper.app.dto.DatabaseResponseDto;
import com.wrapper.app.service.MongoDbService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoDbController {

    private final MongoDbService service;

    private final ModelMapper modelMapper;

    public MongoDbController(MongoDbService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @CrossOrigin(value = "*")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DatabaseResponseDto> getAll() {
        return service.getAll().stream().map(d -> modelMapper.map(d, DatabaseResponseDto.class)).toList();
    }

    @CrossOrigin(value = "*")
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public DatabaseResponseDto create(@RequestBody DatabaseRequestDto requestDto) {
        Database database = service.create(modelMapper.map(requestDto, Database.class));
        return modelMapper.map(database, DatabaseResponseDto.class);
    }

    @CrossOrigin(value = "*")
    @PutMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public DatabaseResponseDto update(@RequestBody DatabaseResponseDto requestDto) {
        Database database = service.update(modelMapper.map(requestDto, Database.class));
        return modelMapper.map(database, DatabaseResponseDto.class);
    }

    @CrossOrigin(value = "*")
    @GetMapping("/switch/{databaseName}")
    @ResponseStatus(HttpStatus.OK)
    public void switchDatabase(@PathVariable String databaseName) {
        service.switchDatabase(databaseName.replace("_", "/"));
    }
}
