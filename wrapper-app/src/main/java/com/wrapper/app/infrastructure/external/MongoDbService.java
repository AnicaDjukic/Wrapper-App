package com.wrapper.app.infrastructure.external;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDbService implements DataService {

    private final MongoTemplate mongoTemplate;

    public MongoDbService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<?> getData(Class<?> entityClass, String from) {
        return mongoTemplate.findAll(entityClass, from);
    }
}
