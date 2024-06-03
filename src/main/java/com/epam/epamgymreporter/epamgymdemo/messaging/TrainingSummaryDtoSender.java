package com.epam.epamgymreporter.epamgymdemo.messaging;

import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.service.TrainingService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingSummaryDtoSender {

    private final TrainingService trainingService;

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = "training.summary.queue")
    public void receiveAndRespond(Message message) throws JMSException {
        String username = message.getBody(String.class);
        TrainingSummaryDto trainingSummaryDto = trainingService.getMonthlyTrainingSummary(username);
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), trainingSummaryDto);
    }
}
