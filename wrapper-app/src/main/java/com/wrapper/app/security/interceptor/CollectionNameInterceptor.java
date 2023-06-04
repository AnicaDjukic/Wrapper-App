package com.wrapper.app.security.interceptor;

import com.wrapper.app.domain.Database;
import com.wrapper.app.repository.CollectionNameProvider;
import com.wrapper.app.security.util.TokenUtils;
import com.wrapper.app.service.MongoDbService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

@Component
public class CollectionNameInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils;

    private final MongoDbService mongoDbService;

    public CollectionNameInterceptor(TokenUtils tokenUtils, MongoDbService mongoDbService) {
        this.tokenUtils = tokenUtils;
        this.mongoDbService = mongoDbService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = getUserIdFromRequest(request);
        String collectionName = CollectionNameProvider.getUserCollection(userId);
        if(collectionName != null) {
            CollectionNameProvider.setCollectionName(CollectionNameProvider.getUserCollection(userId));
        } else {
            List<Database> databases = mongoDbService.getAll();
            Database database = databases.get(databases.size() - 1);
            CollectionNameProvider.setCollectionName(database.getGodina() + database.getSemestar().charAt(0));
        }
        return true;
    }

    private String getUserIdFromRequest(HttpServletRequest request) {
        String authToken = tokenUtils.getToken(request);
        return tokenUtils.getUsernameFromToken(authToken);
    }
}