package com.wrapper.app.infrastructure.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.wrapper.app.infrastructure.persistence.cascade.CascadingMongoDeleteEventListener;
import com.wrapper.app.infrastructure.persistence.cascade.CascadingMongoSaveEventListener;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameResolver;
import com.wrapper.app.infrastructure.persistence.util.CollectionNameResolverImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Override
    protected String getDatabaseName() {
        return databaseName;
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://" + host + ":" + port);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), getDatabaseName());
    }

    @Bean
    public CollectionNameResolver collectionNameResolver() {
        return new CollectionNameResolverImpl() {
            @Override
            public String resolveCollectionName(Class<?> entityClass) {
                return super.resolveCollectionName(entityClass);
            }
        };
    }

    @Bean
    public CascadingMongoSaveEventListener cascadingMongoEventListener() {
        return new CascadingMongoSaveEventListener(mongoTemplate());
    }

    @Bean
    public CascadingMongoDeleteEventListener cascadeDeleteEventListener() {
        return new CascadingMongoDeleteEventListener(mongoTemplate());
    }
}
