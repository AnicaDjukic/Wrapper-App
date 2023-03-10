package com.wrapper.app.repository;

import com.wrapper.app.domain.Dan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DanRepository extends MongoRepository<Dan, Long> {
}
