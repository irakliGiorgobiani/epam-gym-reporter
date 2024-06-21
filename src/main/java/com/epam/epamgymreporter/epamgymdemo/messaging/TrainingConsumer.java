package com.epam.epamgymreporter.epamgymdemo.messaging;

import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.service.TrainingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingConsumer {

    private final TrainingSummaryService trainingSummaryService;

    @JmsListener(destination = "db.operations.queue")
    public void receive(TrainingDto trainingDto) {
        trainingSummaryService.saveTrainingSummary(trainingDto);
    }
}
