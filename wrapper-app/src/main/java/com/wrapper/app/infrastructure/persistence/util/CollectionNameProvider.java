package com.wrapper.app.infrastructure.persistence.util;

import java.util.HashMap;
import java.util.Map;

public class CollectionNameProvider {

    private static final Map<String, String> userCollectionMap = new HashMap<>();

    public static void setUserCollection(String userId, String collectionName) {
        userCollectionMap.put(userId, collectionName);
    }

    public static String getUserCollection(String userId) {
        return userCollectionMap.get(userId);
    }

    private static final ThreadLocal<String> collectionNameThreadLocal = ThreadLocal.withInitial(() -> null);

    public static String getCollectionName() {
        return collectionNameThreadLocal.get();
    }

    public static void setCollectionName(String collectionName) {
        collectionNameThreadLocal.set(collectionName);
    }

}
