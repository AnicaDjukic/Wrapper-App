package com.wrapper.app.config;

import com.wrapper.app.domain.Database;
import com.wrapper.app.repository.util.CollectionNameProvider;
import com.wrapper.app.repository.DatabaseRepository;
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
