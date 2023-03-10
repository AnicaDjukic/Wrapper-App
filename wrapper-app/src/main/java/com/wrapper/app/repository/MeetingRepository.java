package com.wrapper.app.repository;

import com.wrapper.app.domain.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MeetingRepository extends MongoRepository<Meeting, String> {
}
