package com.wrapper.app.repository.cascade;

import org.springframework.context.ApplicationListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.util.ReflectionUtils;
import java.util.*;

public class CascadingMongoEventListener implements ApplicationListener<BeforeSaveEvent<Object>> {

    private final MongoTemplate mongoTemplate;

    public CascadingMongoEventListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public void onApplicationEvent(BeforeSaveEvent<Object> event) {
        Object source = event.getSource();
        processFields(source);
    }

    private void processFields(Object source) {

        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(CascadeSave.class)) {
                final Object fieldValue = field.get(source);

                if (fieldValue != null) {
                    if (field.getType().equals(List.class)) {
                        List<?> list = (List<?>) fieldValue;
                        for (Object item : list) {
                            processFields(item);
                            mongoTemplate.save(item); // Save each item in the list
                        }
                    } else {
                        processFields(fieldValue);
                        mongoTemplate.save(fieldValue); // Save the nested object
                    }
                }
            }
        });
    }
}