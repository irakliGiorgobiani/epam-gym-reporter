package com.epam.epamgymreporter.converter;

import com.epam.epamgymreporter.model.bo.TrainingSummary;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingDtoToSummaryConverter {

    public TrainingSummary convert(TrainingDto trainingDto) {
        Map<Integer, Number> monthSummary = new HashMap<>();
        monthSummary.put(trainingDto.getTrainingDate().getMonthValue(), trainingDto.getTrainingDuration());

        Map<Integer, Map<Integer, Number>> yearlySummary = new HashMap<>();
        yearlySummary.put(trainingDto.getTrainingDate().getYear(), monthSummary);

        return TrainingSummary.builder()
                .username(trainingDto.getUsername())
                .firstName(trainingDto.getFirstName())
                .lastName(trainingDto.getLastName())
                .status(trainingDto.getStatus())
                .yearlySummary(yearlySummary)
                .build();
    }
}
