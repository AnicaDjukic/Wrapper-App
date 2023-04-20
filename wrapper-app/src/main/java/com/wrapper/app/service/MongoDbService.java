package com.wrapper.app.service;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.stereotype.Service;

@Service
public class MongoDbService {

    private final MongoClient mongoClient;

    public MongoDbService() {
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");;
    }

    public void listDatabases() {
        for (String s : mongoClient.listDatabaseNames()) {
            System.out.println(s);
        }
    }

    public Boolean databaseFound(String databaseName){
        for (String s : mongoClient.listDatabaseNames()) {
            if (s.equals(databaseName))
                return true;
        }
        return false;
    }

    public void switchDatabase(String databaseName) {
        if(databaseFound(databaseName)) {
            mongoClient.getDatabase(databaseName);
            System.out.println(mongoClient.startSession());
        }
    }
}
