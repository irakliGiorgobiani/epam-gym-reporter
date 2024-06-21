package com.epam.epamgymreporter.converter;

import com.epam.epamgymreporter.model.bo.TrainingSummary;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import org.springframework.stereotype.Component;

@Component
public class SummaryToSummaryDtoConverter {

    public TrainingSummaryDto convert(TrainingSummary trainingSummary) {
        return TrainingSummaryDto.builder()
                .username(trainingSummary.getUsername())
                .firstName(trainingSummary.getFirstName())
                .lastName(trainingSummary.getLastName())
                .status(trainingSummary.getStatus())
                .yearlySummary(trainingSummary.getYearlySummary())
                .build();
    }
}
