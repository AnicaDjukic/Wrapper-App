package com.wrapper.app.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.wrapper.app.repository.CollectionNameResolver;
import com.wrapper.app.repository.CollectionNameResolverImpl;
import com.wrapper.app.repository.cascade.CascadingMongoEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "WrapperAppDatabase";
    }

    @Bean
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
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
    public CascadingMongoEventListener cascadingMongoEventListener() {
        return new CascadingMongoEventListener();
    }
}
