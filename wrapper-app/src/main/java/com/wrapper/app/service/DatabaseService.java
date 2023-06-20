package com.wrapper.app.service;

import com.wrapper.app.domain.*;
import com.wrapper.app.exception.AlreadyExistsException;
import com.wrapper.app.exception.NotFoundException;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.repository.DatabaseRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DatabaseService<T> {

    private final DataService<T> dataService;

    private final MongoTemplate mongoTemplate;

    private final DatabaseRepository repository;

    private static final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private static final String PREDMETI = "Predmeti";
    private static final String PREDAVACI = "Predavaci";
    private static final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";
    private static final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private static final String PROSTORIJE = "Prostorije";

    public DatabaseService(DataService<T> dataService, MongoTemplate mongoTemplate, DatabaseRepository repository) {
        this.dataService = dataService;
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    public List<Database> getAll() {
        return repository.findAll();
    }

    public Database getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(Database.class.getSimpleName()));
    }

    public void switchDatabase(String userId, String databaseName) {
        CollectionNameProvider.setUserCollection(userId, databaseName);
        CollectionNameProvider.setCollectionName(databaseName);
    }

    public Database create(Database database) {
        Optional<Database> existing = repository.findBySemestarAndGodina(database.getSemestar(), database.getGodina());
        if (existing.isPresent()) {
            throw new AlreadyExistsException(Database.class.getSimpleName());
        }
        String newSemester = database.getGodina() + database.getSemestar().substring(0, 1).toUpperCase();
        CollectionNameProvider.setCollectionName(newSemester);
        createCollections(database, newSemester);
        database.setId(UUID.randomUUID().toString());
        return repository.save(database);
    }

    public Database updateCollections(Database database) {
        Database existing = repository.
                findBySemestarAndGodina(database.getSemestar(), database.getGodina())
                .orElseThrow(() -> new NotFoundException(Database.class.getSimpleName()));
        String databaseName = database.getGodina() + database.getSemestar().substring(0, 1).toUpperCase();
        CollectionNameProvider.setCollectionName(databaseName);
        updateCollections(database, databaseName);
        database.setId(existing.getId());
        return repository.save(database);
    }

    private void createCollections(Database database, String newSemester) {
        createCollection(database.getStudijskiProgrami(), STUDIJSKI_PROGRAMI, newSemester);
        createCollection(database.getPredmeti(), PREDMETI, newSemester);
        createCollection(database.getPredavaci(), PREDAVACI, newSemester);
        createCollection(database.getRealizacija(), STUDIJSKI_PROGRAM_PREDMETI, newSemester);
        createCollection(database.getStudentskeGrupe(), STUDENTSKE_GRUPE, newSemester);
        createCollection(database.getProstorije(), PROSTORIJE, newSemester);
        cleanData(newSemester);
    }

    private void createCollection(String from, String collectionNamePrefix, String newSemester) {
        List<T> data = dataService.getData(getGenericTypeClass(collectionNamePrefix), collectionNamePrefix + from);
        String collectionName = collectionNamePrefix + newSemester;
        mongoTemplate.createCollection(collectionName);
        mongoTemplate.insert(data, collectionName);
    }

    private void cleanData(String newSemester) {
        CollectionNameProvider.setCollectionName(newSemester);
        String collectionName = STUDIJSKI_PROGRAM_PREDMETI + newSemester;
        List<StudijskiProgramPredmeti> studijskiProgramPredmetiList = mongoTemplate.findAll(StudijskiProgramPredmeti.class, collectionName);
        for (StudijskiProgramPredmeti studijskiProgramPredmeti : studijskiProgramPredmetiList) {
            studijskiProgramPredmeti.removeMissingPredavaci();
            mongoTemplate.save(studijskiProgramPredmeti, collectionName);
        }
    }

    private void updateCollections(Database database, String newSemester) {
        updateCollection(database.getStudijskiProgrami(), STUDIJSKI_PROGRAMI, newSemester);
        updateCollection(database.getPredmeti(), PREDMETI, newSemester);
        updateCollection(database.getPredavaci(), PREDAVACI, newSemester);
        updateCollection(database.getRealizacija(), STUDIJSKI_PROGRAM_PREDMETI, newSemester);
        updateCollection(database.getStudentskeGrupe(), STUDENTSKE_GRUPE, newSemester);
        updateCollection(database.getProstorije(), PROSTORIJE, newSemester);
        cleanData(newSemester);
    }

    private void updateCollection(String from, String collectionNamePrefix, String newSemester) {
        String collectionName = collectionNamePrefix + newSemester;
        if (!newSemester.equals(from)) {
            dropCollection(collectionName);
            createCollection(from, collectionNamePrefix, newSemester);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<T> getGenericTypeClass(String collectionName) {
        if (collectionName.startsWith(STUDIJSKI_PROGRAMI)) {
            return (Class<T>) StudijskiProgram.class;
        } else if (collectionName.startsWith(PREDMETI)) {
            return (Class<T>) Predmet.class;
        } else if (collectionName.startsWith(PREDAVACI)) {
            return (Class<T>) Predavac.class;
        } else if (collectionName.startsWith(STUDIJSKI_PROGRAM_PREDMETI)) {
            return (Class<T>) StudijskiProgramPredmeti.class;
        } else if (collectionName.startsWith(STUDENTSKE_GRUPE)) {
            return (Class<T>) StudentskaGrupa.class;
        } else if (collectionName.startsWith(PROSTORIJE)) {
            return (Class<T>) Prostorija.class;
        } else {
            throw new IllegalArgumentException("Invalid collection name: " + collectionName);
        }
    }

    private void dropCollection(String collectionName) {
        if (mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.dropCollection(collectionName);
        } else {
            throw new NotFoundException(collectionName);
        }
    }

    public void update(Database database) {
        repository.save(database);
    }
}
