package com.epam.epamgymreporter.converter;

import com.epam.epamgymreporter.model.bo.Training;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import org.springframework.stereotype.Component;

@Component
public class DtoToBoConverter {

    public Training trainingDtoToTraining(TrainingDto trainingDto) {
        return Training.builder()
                .username(trainingDto.getUsername())
                .firstName(trainingDto.getFirstName())
                .lastName(trainingDto.getLastName())
                .active(trainingDto.getActive())
                .trainingDate(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .build();
    }
}
