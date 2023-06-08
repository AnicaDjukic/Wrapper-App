package com.wrapper.app.repository.cascade;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.PredmetPredavac;
import com.wrapper.app.domain.StudijskiProgramPredmeti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;
import java.util.*;

public class CascadingMongoEventListener extends AbstractMongoEventListener<Object> {

    @Autowired
    private MongoOperations mongoOperations;
    private final Set<Object> processedObjects = new HashSet<>();

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        processFields(source);
    }

    private void processFields(Object source) {
        if (processedObjects.contains(source)) {
            return; // Skip processing if the object has already been processed
        }

        processedObjects.add(source);

        ReflectionUtils.doWithFields(source.getClass(), field -> {
            ReflectionUtils.makeAccessible(field);

            if (field.isAnnotationPresent(DocumentReference.class) && field.isAnnotationPresent(CascadeSave.class)) {
                final Object fieldValue = field.get(source);

                if (fieldValue != null) {
                    if (field.getType().equals(List.class)) {
                        List<?> list = (List<?>) fieldValue;
                        for (Object item : list) {
                            processFields(item);
                            mongoOperations.save(item); // Save each item in the list
                        }
                    } else {
                        processFields(fieldValue);
                        mongoOperations.save(fieldValue); // Save the nested object
                    }
                }
            }
        });

        if (source instanceof StudijskiProgramPredmeti studijskiProgramPredmeti) {
            List<PredmetPredavac> predmetPredavaci = studijskiProgramPredmeti.getPredmetPredavaci();
            for (PredmetPredavac predmetPredavac : predmetPredavaci) {
                Predmet predmet = predmetPredavac.getPredmet();
                processFields(predmet);
                mongoOperations.save(predmet); // Save the nested Predmet object
            }
        }
    }
}