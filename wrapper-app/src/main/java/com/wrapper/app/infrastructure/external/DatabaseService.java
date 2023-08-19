package com.wrapper.app.infrastructure.external;

import com.wrapper.app.domain.exception.AlreadyExistsException;
import com.wrapper.app.domain.exception.NotFoundException;
import com.wrapper.app.domain.model.*;
import com.wrapper.app.infrastructure.persistence.repository.DatabaseRepository;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameProvider;
import com.wrapper.app.infrastructure.persistence.util.CollectionTypes;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DatabaseService<T> {

    private final DataService<T> dataService;

    private final MongoTemplate mongoTemplate;

    private final DatabaseRepository repository;

    public DatabaseService(DataService<T> dataService, MongoTemplate mongoTemplate, DatabaseRepository repository) {
        this.dataService = dataService;
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    public List<Database> getAll() {
        return repository.findAll();
    }

    public List<Database> getUnblocked() {
        List<Database> unblockedDatabases = new ArrayList<>(repository.findAll());
        for (Database database: repository.findAll()) {
            String collectionName = CollectionTypes.STUDIJSKI_PROGRAMI + database.getGodina() + database.getSemestar().charAt(0);
            List<StudijskiProgram> studijskiProgrami = mongoTemplate.findAll(StudijskiProgram.class, collectionName);
            boolean isBlocked = studijskiProgrami.stream().anyMatch(StudijskiProgram::isBlock);
            if(isBlocked) {
                unblockedDatabases.remove(database);
            }
        }
        return unblockedDatabases;
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
        database.setStatus(GenerationStatus.NOT_STARTED);
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
        createCollection(database.getStudijskiProgrami(), CollectionTypes.STUDIJSKI_PROGRAMI, newSemester);
        createCollection(database.getPredmeti(), CollectionTypes.PREDMETI, newSemester);
        createCollection(database.getPredavaci(), CollectionTypes.PREDAVACI, newSemester);
        createCollection(database.getRealizacija(), CollectionTypes.STUDIJSKI_PROGRAM_PREDMETI, newSemester);
        createCollection(database.getStudentskeGrupe(), CollectionTypes.STUDENTSKE_GRUPE, newSemester);
        createCollection(database.getProstorije(), CollectionTypes.PROSTORIJE, newSemester);
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
        String collectionName = CollectionTypes.STUDIJSKI_PROGRAM_PREDMETI + newSemester;
        List<StudijskiProgramPredmeti> studijskiProgramPredmetiList = mongoTemplate.findAll(StudijskiProgramPredmeti.class, collectionName);
        for (StudijskiProgramPredmeti studijskiProgramPredmeti : studijskiProgramPredmetiList) {
            studijskiProgramPredmeti.removeMissingPredavaci();
            mongoTemplate.save(studijskiProgramPredmeti, collectionName);
        }
    }

    private void updateCollections(Database database, String newSemester) {
        updateCollection(database.getStudijskiProgrami(), CollectionTypes.STUDIJSKI_PROGRAMI, newSemester);
        updateCollection(database.getPredmeti(), CollectionTypes.PREDMETI, newSemester);
        updateCollection(database.getPredavaci(), CollectionTypes.PREDAVACI, newSemester);
        updateCollection(database.getRealizacija(), CollectionTypes.STUDIJSKI_PROGRAM_PREDMETI, newSemester);
        updateCollection(database.getStudentskeGrupe(), CollectionTypes.STUDENTSKE_GRUPE, newSemester);
        updateCollection(database.getProstorije(), CollectionTypes.PROSTORIJE, newSemester);
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
        if (collectionName.startsWith(CollectionTypes.STUDIJSKI_PROGRAMI)) {
            return (Class<T>) StudijskiProgram.class;
        } else if (collectionName.startsWith(CollectionTypes.PREDMETI)) {
            return (Class<T>) Predmet.class;
        } else if (collectionName.startsWith(CollectionTypes.PREDAVACI)) {
            return (Class<T>) Predavac.class;
        } else if (collectionName.startsWith(CollectionTypes.STUDIJSKI_PROGRAM_PREDMETI)) {
            return (Class<T>) StudijskiProgramPredmeti.class;
        } else if (collectionName.startsWith(CollectionTypes.STUDENTSKE_GRUPE)) {
            return (Class<T>) StudentskaGrupa.class;
        } else if (collectionName.startsWith(CollectionTypes.PROSTORIJE)) {
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

    public Database update(Database database) {
        return repository.save(database);
    }

    public Database getUnfinished() {
        Optional<Database> started = repository.findByStatus(GenerationStatus.STARTED);
        if(started.isEmpty())
            return repository.findByStatus(GenerationStatus.OPTIMIZING)
                .orElseThrow(() -> new NotFoundException(Database.class.getSimpleName()));
        return started.get();
    }
}
