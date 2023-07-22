package com.wrapper.app.repository.util;

import com.wrapper.app.domain.*;
import org.springframework.stereotype.Component;

@Component
public class CollectionNameResolverImpl implements CollectionNameResolver {

    private static final String PREDMETI = "Predmeti";
    private static final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private static final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private static final String PREDAVACI = "Predavaci";
    private static final String PROSTORIJE = "Prostorije";
    private static final String STUDIJSKI_PROGRAM_PREDMETI = "StudijskiProgramPredmeti";

    @Override
    public String resolveCollectionName(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        String collectionName = CollectionNameProvider.getCollectionName();
        return getCollectionName(className, collectionName);
    }

    private String getCollectionName(String className, String collectionName) {
        if (className.equals(Predmet.class.getSimpleName())) {
            return PREDMETI + collectionName;
        } else if (className.equals(StudijskiProgram.class.getSimpleName()))  {
            return STUDIJSKI_PROGRAMI + collectionName;
        } else if (className.equals(StudentskaGrupa.class.getSimpleName())) {
            return STUDENTSKE_GRUPE + collectionName;
        } else if (className.equals(Predavac.class.getSimpleName())) {
            return PREDAVACI + collectionName;
        } else if (className.equals(Prostorija.class.getSimpleName())) {
            return PROSTORIJE + collectionName;
        } else if (className.equals(StudijskiProgramPredmeti.class.getSimpleName())) {
            return STUDIJSKI_PROGRAM_PREDMETI + collectionName;
        } else {
            return null;
        }
    }
}
