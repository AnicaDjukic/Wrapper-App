package com.wrapper.app.infrastructure.persistence.util;


public interface CollectionNameResolver {

    String resolveCollectionName(Class<?> entityClass);
}
