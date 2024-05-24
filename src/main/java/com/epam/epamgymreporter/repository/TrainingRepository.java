package com.epam.epamgymreporter.repository;

import com.epam.epamgymreporter.model.bo.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    List<Training> getTrainingByUsername(String username);

    void deleteByUsernameAndTrainingDateAndTrainingDuration(String username, LocalDate trainingDate,
                                                                           Double trainingDuration);
}
