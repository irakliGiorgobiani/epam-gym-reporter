package com.epam.epamgymreporter.repository;

import com.epam.epamgymreporter.model.bo.TrainingSummary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TrainingSummaryRepository extends MongoRepository<TrainingSummary, ObjectId> {
    Optional<TrainingSummary> findByUsername(String username);
}
