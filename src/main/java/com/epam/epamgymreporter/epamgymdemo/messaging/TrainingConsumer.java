package com.epam.epamgymreporter.epamgymdemo.messaging;

import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingConsumer {

    private final TrainingService trainingService;

    @JmsListener(destination = "db.operations.queue")
    public void receive(TrainingDto trainingDto) {
        trainingService.saveTraining(trainingDto);
    }
}
