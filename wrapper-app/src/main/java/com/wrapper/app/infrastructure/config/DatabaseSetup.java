package com.wrapper.app.infrastructure.config;

import com.wrapper.app.domain.model.Database;
import com.wrapper.app.infrastructure.persistence.repository.DatabaseRepository;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSetup {

    private final DatabaseRepository databaseRepository;

    public DatabaseSetup(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        List<Database> databases = databaseRepository.findAll();
        Database database = databases.get(databases.size() - 1);
        CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
    }
}
