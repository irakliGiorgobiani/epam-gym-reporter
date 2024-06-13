package com.epam.epamgymreporter.service;

import com.epam.epamgymreporter.converter.DtoToBoConverter;
import com.epam.epamgymreporter.epamgymdemo.messaging.TrainingSummaryProducer;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.model.bo.Training;
import com.epam.epamgymreporter.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    private final DtoToBoConverter converter;

    private final TrainingSummaryProducer trainingSummaryProducer;

    private static final String ADD = "ADD";

    private static final String DELETE = "DELETE";

    private static final String ACTIVE = "Active";

    private static final String NOT_ACTIVE = "Not Active";


    @Transactional
    public void saveTraining(TrainingDto trainingDto) {
        if (trainingDto == null) {
            throw new IllegalArgumentException("Required training is missing, please insert a training instance");
        }

        Training training = converter.trainingDtoToTraining(trainingDto);

        if (trainingDto.getActionType().equals(ADD)) {
            trainingRepository.save(training);
        } else if (trainingDto.getActionType().equals(DELETE)) {
            trainingRepository.deleteByUsernameAndTrainingDateAndTrainingDuration(training.getUsername(),
                    training.getTrainingDate(),
                    training.getTrainingDuration());
        }
    }

    public TrainingSummaryDto getMonthlyTrainingSummary(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username is missing, please provide a valid username");
        }

        List<Training> trainings = trainingRepository.getTrainingByUsername(username);

        String status = trainings.isEmpty() ? NOT_ACTIVE : ACTIVE;

        Map<Integer, Map<Integer, Double>> yearlySummary = trainings.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTrainingDate().getYear(),
                        Collectors.groupingBy(
                                t -> t.getTrainingDate().getMonthValue(),
                                HashMap::new,
                                Collectors.summingDouble(Training::getTrainingDuration)
                        )
                ));

        return TrainingSummaryDto.builder()
                .username(username)
                .firstName(trainings.isEmpty() ? "" : trainings.get(0).getFirstName())
                .lastName(trainings.isEmpty() ? "" : trainings.get(0).getLastName())
                .status(status)
                .yearlySummary(yearlySummary)
                .build();
    }

    public void sendTrainingSummary(String username) {
        trainingSummaryProducer.send(this.getMonthlyTrainingSummary(username));
    }
}
