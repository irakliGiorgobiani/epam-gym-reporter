package com.epam.epamgymreporter.service;

import com.epam.epamgymreporter.converter.SummaryToSummaryDtoConverter;
import com.epam.epamgymreporter.converter.TrainingDtoToSummaryConverter;
import com.epam.epamgymreporter.epamgymdemo.messaging.TrainingSummaryProducer;
import com.epam.epamgymreporter.exception.RepositoryInconsistencyException;
import com.epam.epamgymreporter.exception.UsernameNotFoundException;
import com.epam.epamgymreporter.model.bo.TrainingSummary;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.repository.TrainingSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TrainingSummaryService {

    private final TrainingSummaryRepository trainingSummaryRepository;

    private final TrainingSummaryProducer trainingSummaryProducer;

    private final TrainingDtoToSummaryConverter trainingDtoToSummaryConverter;

    private final SummaryToSummaryDtoConverter summaryToSummaryDtoConverter;

    public void saveTrainingSummary(TrainingDto trainingDto) {
        if (trainingSummaryRepository.findByUsername(trainingDto.getUsername()).isPresent()) {
            trainingSummaryRepository.save(updateTrainingSummary(findByUsername(trainingDto.getUsername()),
                    trainingDto));
        } else {
            trainingSummaryRepository.save(trainingDtoToSummaryConverter.convert(trainingDto));
        }
    }

    private TrainingSummary findByUsername(String username) {
        return trainingSummaryRepository.findByUsername(username)
                .orElseThrow(() -> new RepositoryInconsistencyException("Something went wrong, try again."));
    }

    private TrainingSummary updateTrainingSummary(TrainingSummary trainingSummary, TrainingDto trainingDto) {
        trainingSummary.setStatus(trainingDto.getStatus());
        trainingSummary.getYearlySummary()
                .computeIfAbsent(trainingDto.getTrainingDate().getYear(),
                        k -> new HashMap<>())
                .merge(trainingDto.getTrainingDate().getMonthValue(),
                        trainingDto.getTrainingDuration(),
                        (existingValue, newValue) -> existingValue.doubleValue() + newValue.doubleValue());
        return trainingSummary;
    }

    public void sendTrainingSummary(String username) {
        if (trainingSummaryRepository.findByUsername(username).isPresent()) {
            trainingSummaryProducer.send(summaryToSummaryDtoConverter.convert(findByUsername(username)));
        } else throw new UsernameNotFoundException("Provided username does not exist.");
    }
}
