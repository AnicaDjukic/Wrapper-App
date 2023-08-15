package com.wrapper.app.infrastructure.persistence.util;

import com.wrapper.app.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class CollectionNameResolverImpl implements CollectionNameResolver {

    @Override
    public String resolveCollectionName(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        String collectionName = CollectionNameProvider.getCollectionName();
        return getCollectionName(className, collectionName);
    }

    private String getCollectionName(String className, String collectionName) {
        if (className.equals(Predmet.class.getSimpleName())) {
            return CollectionTypes.PREDMETI + collectionName;
        } else if (className.equals(StudijskiProgram.class.getSimpleName()))  {
            return CollectionTypes.STUDIJSKI_PROGRAMI + collectionName;
        } else if (className.equals(StudentskaGrupa.class.getSimpleName())) {
            return CollectionTypes.STUDENTSKE_GRUPE + collectionName;
        } else if (className.equals(Predavac.class.getSimpleName())) {
            return CollectionTypes.PREDAVACI + collectionName;
        } else if (className.equals(Prostorija.class.getSimpleName())) {
            return CollectionTypes.PROSTORIJE + collectionName;
        } else if (className.equals(StudijskiProgramPredmeti.class.getSimpleName())) {
            return CollectionTypes.STUDIJSKI_PROGRAM_PREDMETI + collectionName;
        } else if (className.equals(Meeting.class.getSimpleName())) {
            return CollectionTypes.MEETINGS + collectionName;
        } else {
            return null;
        }
    }
}
