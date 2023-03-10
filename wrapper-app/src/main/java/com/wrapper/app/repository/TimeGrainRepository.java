package com.wrapper.app.repository;

import com.wrapper.app.domain.TimeGrain;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TimeGrainRepository extends MongoRepository<TimeGrain, String> {
}
