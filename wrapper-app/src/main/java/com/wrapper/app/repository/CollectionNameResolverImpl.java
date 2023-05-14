package com.wrapper.app.repository;

import com.wrapper.app.domain.*;
import org.springframework.stereotype.Component;

@Component
public class CollectionNameResolverImpl implements CollectionNameResolver {

    private final String PREDMETI = "Predmeti";
    private final String STUDIJSKI_PROGRAMI = "StudijskiProgrami";
    private final String STUDENTSKE_GRUPE = "StudentskeGrupe";
    private final String REALIZACIJA = "Realizacija";
    private final String PREDAVACI = "Predavaci";
    private final String PROSTORIJE = "Prostorije";

    @Override
    public String resolveCollectionName(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        String collectionName = CollectionNameProvider.getCollectionName();

        if (className.equals(Predmet.class.getSimpleName())) {
            return PREDMETI + collectionName;
        } else if (className.equals(StudijskiProgram.class.getSimpleName()))  {
            return STUDIJSKI_PROGRAMI + collectionName;
        } else if (className.equals(StudentskaGrupa.class.getSimpleName())) {
            return STUDENTSKE_GRUPE + collectionName;
        } else if (className.equals(Realizacija.class.getSimpleName())) {
            return REALIZACIJA + collectionName;
        } else if (className.equals(Predavac.class.getSimpleName())) {
            return PREDAVACI + collectionName;
        } else if (className.equals(Prostorija.class.getSimpleName())) {
            return PROSTORIJE + collectionName;
        } else {
            return null;
        }
    }
}
