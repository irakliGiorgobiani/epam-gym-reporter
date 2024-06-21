package com.epam.epamgymreporter.service;

import com.epam.epamgymreporter.converter.SummaryToSummaryDtoConverter;
import com.epam.epamgymreporter.converter.TrainingDtoToSummaryConverter;
import com.epam.epamgymreporter.epamgymdemo.messaging.TrainingSummaryProducer;
import com.epam.epamgymreporter.model.bo.TrainingSummary;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.repository.TrainingSummaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingSummaryServiceTest {

    @Mock
    private TrainingSummaryRepository trainingSummaryRepository;

    @Mock
    private TrainingSummaryProducer trainingSummaryProducer;

    @Mock
    private TrainingDtoToSummaryConverter trainingDtoToSummaryConverter;

    @Mock
    private SummaryToSummaryDtoConverter summaryToSummaryDtoConverter;

    @InjectMocks
    private TrainingSummaryService trainingSummaryService;

    private static final TrainingSummary summary;

    private static final String username;

    private static final TrainingDto trainingDto;

    static {
        username = "username";

        HashMap<Integer, Map<Integer, Number>> yearlySummary = new HashMap<>();
        yearlySummary.put(2024, new HashMap<>());

        summary = TrainingSummary.builder()
                .username(username)
                .firstName("firstName")
                .lastName("lastName")
                .status(true)
                .yearlySummary(yearlySummary)
                .build();

        trainingDto = TrainingDto.builder()
                .username(username)
                .trainingDate(LocalDate.now())
                .status(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(60)
                .build();
    }

    @Test
    void testUpdatingTrainingSummary() {
        when(trainingSummaryRepository.findByUsername(trainingDto.getUsername())).thenReturn(Optional.of(summary));

        trainingSummaryService.saveTrainingSummary(trainingDto);

        verify(trainingSummaryRepository).save(summary);
        assertEquals(Objects.requireNonNull(trainingSummaryRepository.findByUsername(username).orElse(null))
                .getUsername(), username);
    }

    @Test
    void testCreatingTrainingSummary() {
        when(trainingSummaryRepository.findByUsername(trainingDto.getUsername())).thenReturn(Optional.empty());
        when(trainingDtoToSummaryConverter.convert(trainingDto)).thenReturn(summary);

        trainingSummaryService.saveTrainingSummary(trainingDto);

        verify(trainingSummaryRepository).save(summary);
    }

    @Test
    void testSendTrainingSummary() {
        summary.setUsername(username);
        TrainingSummaryDto trainingSummaryDto = TrainingSummaryDto.builder().username(username).build();

        when(trainingSummaryRepository.findByUsername(username)).thenReturn(Optional.of(summary));
        when(summaryToSummaryDtoConverter.convert(summary)).thenReturn(trainingSummaryDto);

        trainingSummaryService.sendTrainingSummary(username);

        verify(trainingSummaryProducer).send(any());
    }
}
