package com.wrapper.app.presentation.controller;

import com.wrapper.app.domain.model.Database;
import com.wrapper.app.presentation.dto.request.DatabaseRequestDto;
import com.wrapper.app.presentation.dto.response.DatabaseResponseDto;
import com.wrapper.app.infrastructure.security.util.TokenUtils;
import com.wrapper.app.infrastructure.external.DatabaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/databases")
public class DatabaseController<T> {

    private final DatabaseService<T> service;

    private final ModelMapper modelMapper;

    private final TokenUtils tokenUtils;

    public DatabaseController(DatabaseService<T> service, ModelMapper modelMapper, TokenUtils tokenUtils) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.tokenUtils = tokenUtils;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DatabaseResponseDto> getAll() {
        return service.getAll().stream().map(d -> modelMapper.map(d, DatabaseResponseDto.class)).toList();
    }

    @GetMapping("/unblocked")
    @ResponseStatus(HttpStatus.OK)
    public  List<DatabaseResponseDto> getAllUnblocked() {
        return service.getUnblocked().stream().map(d -> modelMapper.map(d, DatabaseResponseDto.class)).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public DatabaseResponseDto create(@RequestBody DatabaseRequestDto requestDto) {
        Database database = service.create(modelMapper.map(requestDto, Database.class));
        return modelMapper.map(database, DatabaseResponseDto.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DatabaseResponseDto update(@RequestBody DatabaseResponseDto requestDto) {
        Database database = service.updateCollections(modelMapper.map(requestDto, Database.class));
        return modelMapper.map(database, DatabaseResponseDto.class);
    }

    @GetMapping("/switch/{databaseName}")
    @ResponseStatus(HttpStatus.OK)
    public void switchDatabase(@PathVariable String databaseName, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String userId = tokenUtils.getUsernameFromToken(token);
        service.switchDatabase(userId, databaseName.replace("_", "/"));
    }
}
