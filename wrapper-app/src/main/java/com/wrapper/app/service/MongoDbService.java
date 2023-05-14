package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.repository.DatabaseRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MongoDbService {

    private final MongoTemplate mongoTemplate;

    private final DatabaseRepository repository;

    private final String PREDMETI = "Predmeti";
    private final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private final String REALIZACIJA = "Realizacija";
    private final String PREDAVACI = "Predavaci";
    private final String PROSTORIJE = "Prostorije";

    public MongoDbService(MongoTemplate mongoTemplate, DatabaseRepository repository) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    public List<Database> getAll() {
        return repository.findAll();
    }

    public void switchDatabase(String databaseName) {
        CollectionNameProvider.setCollectionName(databaseName);
    }

    public Database create(Database database) {
        createCollections(database);
        Optional<Database> existing = repository.findBySemestarAndGodina(database.getSemestar(), database.getGodina());
        if(existing.isPresent()) {
            throw new AlreadyExistsException(Database.class.getSimpleName());
        }
        database.setId(UUID.randomUUID().toString());
        repository.save(database);
        return database;
    }

    private void createCollections(Database database) {
        String newDatabaseName = database.getGodina()
                + database.getSemestar().substring(0,1).toUpperCase();
        createPredmetiCollection(database.getPredmeti(), newDatabaseName);
        createStudijskiProgramiCollection(database.getStudijskiProgrami(), newDatabaseName);
        createStudentskeGrupeCollection(database.getStudentskeGrupe(), newDatabaseName);
        createRealizacijaCollection(database);
        createPredavaciCollection(database.getPredavaci(), newDatabaseName);
        createProstorijeCollection(database.getProstorije(), newDatabaseName);
    }

    private void createPredmetiCollection(String fromDatabase, String newDatabase) {
        List<Predmet> predmeti = mongoTemplate.findAll(Predmet.class, PREDMETI + fromDatabase);
        String collectionName = PREDMETI + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(predmeti, collectionName);
    }

    private void createStudijskiProgramiCollection(String fromDatabase, String newDatabase) {
        List<StudijskiProgram> studijskiProgrami = mongoTemplate.findAll(StudijskiProgram.class,
                STUDIJSKI_PROGRAMI + fromDatabase);
        String collectionName = STUDIJSKI_PROGRAMI + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(studijskiProgrami, collectionName);
    }

    private void createStudentskeGrupeCollection(String fromDatabase, String newDatabase) {
        List<StudentskaGrupa> studentskeGrupe = mongoTemplate.findAll(StudentskaGrupa.class,
                STUDENTSKE_GRUPE + fromDatabase);
        String collectionName = STUDENTSKE_GRUPE + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(studentskeGrupe, collectionName);
    }

    private void createRealizacijaCollection(Database database) {
        Realizacija realizacija = mongoTemplate.findAll(Realizacija.class,
                REALIZACIJA + database.getRealizacija()).get(0);
        realizacija.setSemestar(database.getSemestar());
        realizacija.setGodina(database.getGodina());
        String collectionName = REALIZACIJA + database.getGodina()
                + database.getSemestar().substring(0,1).toUpperCase();
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(realizacija, collectionName);
    }

    private void createPredavaciCollection(String fromDatabase, String newDatabase) {
        List<Predavac> predavaci = mongoTemplate.findAll(Predavac.class,
                PREDAVACI + fromDatabase);
        String collectionName = PREDAVACI + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(predavaci, collectionName);
    }

    private void createProstorijeCollection(String fromDatabase, String newDatabase) {
        List<Prostorija> prostorije = mongoTemplate.findAll(Prostorija.class,
                PROSTORIJE + fromDatabase);
        String collectionName = PROSTORIJE + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(prostorije, collectionName);
    }
}
