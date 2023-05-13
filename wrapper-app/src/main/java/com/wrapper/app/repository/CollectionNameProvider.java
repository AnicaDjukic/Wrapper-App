package com.wrapper.app.repository;

import java.util.concurrent.atomic.AtomicReference;

public class CollectionNameProvider {

    private static final AtomicReference<String> collectionName = new AtomicReference<>();

    public static String getCollectionName() {
        return collectionName.get();
    }

    public static void setCollectionName(String name) {
        collectionName.set(name);
    }
}
