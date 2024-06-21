package com.epam.epamgymreporter.epamgymdemo.messaging;

import com.epam.epamgymreporter.service.TrainingSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingSummaryConsumer {

    private final TrainingSummaryService trainingSummaryService;

    @JmsListener(destination = "training.summary.queue")
    public void receive(String username) {
        trainingSummaryService.sendTrainingSummary(username);
    }
}
