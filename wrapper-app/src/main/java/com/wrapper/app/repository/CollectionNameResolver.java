package com.wrapper.app.repository;


public interface CollectionNameResolver {

    String resolveCollectionName(Class<?> entityClass);
}
