package com.epam.epamgymreporter.epamgymdemo.messaging;

import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingSummaryDtoProducer {

    private static final String TRAINING_SUMMARY_RESPONSE = "training.summary.response.queue";

    private final JmsTemplate jmsTemplate;

    public void send(TrainingSummaryDto trainingSummaryDto) {
        jmsTemplate.convertAndSend(TRAINING_SUMMARY_RESPONSE, trainingSummaryDto);
    }
}
