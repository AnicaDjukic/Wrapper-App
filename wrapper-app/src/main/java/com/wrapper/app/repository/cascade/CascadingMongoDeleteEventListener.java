package com.wrapper.app.repository.cascade;

import org.bson.Document;
import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.util.ReflectionUtils;

import java.util.List;

public class CascadingMongoDeleteEventListener implements ApplicationListener<BeforeDeleteEvent<Object>> {

    private final MongoTemplate mongoTemplate;

    public CascadingMongoDeleteEventListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onApplicationEvent(BeforeDeleteEvent<Object> event) {
        Document deletedEntityWithId = event.getDocument();
        String entityId = deletedEntityWithId.get("_id").toString();
        Document deletedDocument = mongoTemplate.findById(entityId, Document.class, event.getCollectionName());
        Object deletedObject = mongoTemplate.getConverter().read(event.getType(), deletedDocument);
        processFields(deletedObject);
    }

    private void processFields(Object source) {
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(CascadeDelete.class)) {
                final Object fieldValue = field.get(source);

                if (fieldValue != null) {
                    if (field.getType().equals(List.class)) {
                        List<?> list = (List<?>) fieldValue;
                        for (Object item : list) {
                            processFields(item);
                            if (field.isAnnotationPresent(DocumentReference.class)) {
                                mongoTemplate.remove(item); // Delete each item in the list
                            }
                        }
                    } else {
                        processFields(fieldValue);
                        if (field.isAnnotationPresent(DocumentReference.class)) {
                            mongoTemplate.remove(fieldValue); // Delete the nested object
                        }
                    }
                }
            }
        });
    }
}
