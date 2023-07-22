package com.wrapper.app.repository.util;


public interface CollectionNameResolver {

    String resolveCollectionName(Class<?> entityClass);
}
