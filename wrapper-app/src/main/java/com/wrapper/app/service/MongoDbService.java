package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
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
    private final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";
    private final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private final String PREDAVACI = "Predavaci";
    private final String PROSTORIJE = "Prostorije";

    public MongoDbService(MongoTemplate mongoTemplate, DatabaseRepository repository) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    public List<Database> getAll() {
        return repository.findAll();
    }

    public void switchDatabase(String userId, String databaseName) {
        CollectionNameProvider.setUserCollection(userId, databaseName);
        CollectionNameProvider.setCollectionName(databaseName);
    }

    public Database create(Database database) {
        Optional<Database> existing = repository.findBySemestarAndGodina(database.getSemestar(),
                database.getGodina());
        if (existing.isPresent()) {
            throw new AlreadyExistsException(Database.class.getSimpleName());
        }
        String newDatabaseName = database.getGodina() + database.getSemestar().substring(0, 1).toUpperCase();
        CollectionNameProvider.setCollectionName(newDatabaseName);
        createCollections(database);
        database.setId(UUID.randomUUID().toString());
        return repository.save(database);
    }

    public Database update(Database database) {
        Optional<Database> existing = repository.findBySemestarAndGodina(database.getSemestar(), database.getGodina());
        if (existing.isEmpty()) {
            throw new NotFoundException(Database.class.getSimpleName());
        }
        String databaseName = database.getGodina() + database.getSemestar().substring(0, 1).toUpperCase();
        CollectionNameProvider.setCollectionName(databaseName);
        updateCollections(database);
        database.setId(existing.get().getId());
        return repository.save(database);
    }

    private void updateCollections(Database database) {
        updateCollection(database, STUDIJSKI_PROGRAMI);
        updateCollection(database, PREDMETI);
        updateCollection(database, STUDENTSKE_GRUPE);
        updateCollection(database, PREDAVACI);
        updateCollection(database, PROSTORIJE);
    }

    private void updateCollection(Database database, String collection) {
        String databaseName = database.getGodina() + database.getSemestar().substring(0, 1).toUpperCase();
        dropCollection(collection + databaseName);
        switch (collection) {
            case STUDIJSKI_PROGRAMI -> createStudijskiProgramiCollection(database.getStudijskiProgrami(), databaseName);
            case PREDMETI -> createPredmetiCollection(database.getPredmeti(), databaseName);
            case STUDENTSKE_GRUPE -> createStudentskeGrupeCollection(database.getStudentskeGrupe(), databaseName);
            case PREDAVACI -> createPredavaciCollection(database.getPredavaci(), databaseName);
            default -> createProstorijeCollection(database.getProstorije(), databaseName);
        }
    }

    private void dropCollection(String collectionName) {
        if (mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.dropCollection(collectionName);
        } else {
            throw new NotFoundException(collectionName);
        }
    }

    private void createCollections(Database database) {
        String newDatabaseName = database.getGodina() + database.getSemestar().substring(0, 1).toUpperCase();
        createStudijskiProgramiCollection(database.getStudijskiProgrami(), newDatabaseName);
        createPredmetiCollection(database.getPredmeti(), newDatabaseName);
        createRealizacijaCollection(database.getRealizacija(), newDatabaseName);
        createStudentskeGrupeCollection(database.getStudentskeGrupe(), newDatabaseName);
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

    private void createRealizacijaCollection(String fromDatabase, String newDatabase) {
        List<StudijskiProgramPredmeti> studijskiProgramPredmeti = mongoTemplate.findAll(StudijskiProgramPredmeti.class,
                STUDIJSKI_PROGRAM_PREDMETI + fromDatabase);
        String collectionName = STUDIJSKI_PROGRAM_PREDMETI + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(studijskiProgramPredmeti, collectionName);

    }

    private void createStudentskeGrupeCollection(String fromDatabase, String newDatabase) {
        List<StudentskaGrupa> studentskeGrupe = mongoTemplate.findAll(StudentskaGrupa.class,
                STUDENTSKE_GRUPE + fromDatabase);
        String collectionName = STUDENTSKE_GRUPE + newDatabase;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(studentskeGrupe, collectionName);
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
