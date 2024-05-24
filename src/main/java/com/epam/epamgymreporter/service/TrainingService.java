package com.epam.epamgymreporter.service;

import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.model.bo.Training;
import com.epam.epamgymreporter.repository.TrainingRepository;
import jakarta.ws.rs.NotFoundException;
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

    private Training convertDtoToModel(TrainingDto trainingDto) {
        return Training.builder()
                .username(trainingDto.getUsername())
                .firstName(trainingDto.getFirstName())
                .lastName(trainingDto.getLastName())
                .active(trainingDto.getActive())
                .trainingDate(trainingDto.getTrainingDate())
                .trainingDuration(trainingDto.getTrainingDuration())
                .build();
    }

    @Transactional
    public void saveTraining(TrainingDto trainingDto) {
        Training training = convertDtoToModel(trainingDto);

        if (trainingDto.getActionType().equals("ADD")) {
            trainingRepository.save(training);
        } else if (trainingDto.getActionType().equals("DELETE")) {
            trainingRepository.deleteByUsernameAndTrainingDateAndTrainingDuration(training.getUsername(),
                    training.getTrainingDate(),
                    training.getTrainingDuration());
        }
    }

    public TrainingSummaryDto getMonthlyTrainingSummary(String username) {
        List<Training> trainings = trainingRepository.getTrainingByUsername(username);

        if (trainings.size() == 0) {
            throw new NotFoundException("trainer with that Username does not exist or has no training scheduled");
        }

        Training training = trainings.get(0);

        String status;

        if (training.getActive()) {
            status = "Active";
        } else status = "Not Active";

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
                .firstName(training.getFirstName())
                .lastName(training.getLastName())
                .status(status)
                .yearlySummary(yearlySummary)
                .build();
    }
}
