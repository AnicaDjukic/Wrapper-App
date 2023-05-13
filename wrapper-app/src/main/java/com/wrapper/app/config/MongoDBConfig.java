package com.wrapper.app.config;

import com.wrapper.app.repository.CollectionNameResolver;
import com.wrapper.app.repository.CollectionNameResolverImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoDBConfig {

    @Bean
    public CollectionNameResolver collectionNameResolver() {
        return new CollectionNameResolverImpl() {
            @Override
            public String resolveCollectionName(Class<?> entityClass) {
                return super.resolveCollectionName(entityClass);
            }
        };
    }
}
