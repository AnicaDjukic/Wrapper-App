package com.wrapper.app.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CollectionNameProvider {

    private static final Map<String, String> userCollectionMap = new HashMap<>();

    public static void setUserCollection(String userId, String collectionName) {
        userCollectionMap.put(userId, collectionName);
    }

    public static String getUserCollection(String userId) {
        return userCollectionMap.get(userId);
    }

    private static final AtomicReference<String> collectionName = new AtomicReference<>();

    public static String getCollectionName() {
        return collectionName.get();
    }

    public static void setCollectionName(String name) {
        collectionName.set(name);
    }
}
