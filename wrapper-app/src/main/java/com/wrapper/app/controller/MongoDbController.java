package com.wrapper.app.controller;

import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.service.MongoDbService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mongo")
public class MongoDbController {

    private final MongoDbService service;

    public MongoDbController(MongoDbService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getAll() {
        service.listDatabases();
    }

    @CrossOrigin(value = "*")
    @GetMapping("/switch/{databaseName}")
    @ResponseStatus(HttpStatus.OK)
    public void switchDatabase(@PathVariable String databaseName) {
        CollectionNameProvider.setCollectionName(databaseName);
    }

    @CrossOrigin(value = "*")
    @GetMapping("/switch/")
    @ResponseStatus(HttpStatus.OK)
    public void switchDatabase() {
        CollectionNameProvider.setCollectionName("");
    }
}
