package com.wrapper.app.repository;

import com.wrapper.app.domain.Predmet;
import com.wrapper.app.domain.StudijskiProgram;
import org.springframework.stereotype.Component;

@Component
public class CollectionNameResolverImpl implements CollectionNameResolver {

    @Override
    public String resolveCollectionName(Class<?> entityClass) {
        String className = entityClass.getSimpleName();
        String collectionName = CollectionNameProvider.getCollectionName();
        if (className.equals(Predmet.class.getSimpleName())) {
            return "Predmeti" + collectionName;
        } else if(className.equals(StudijskiProgram.class.getSimpleName()))  {
            return "StudijskiProgrami" + collectionName;
        }
        return null;
    }
}
