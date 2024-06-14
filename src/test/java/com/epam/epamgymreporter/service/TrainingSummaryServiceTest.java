package com.epam.epamgymreporter.service;

import com.epam.epamgymreporter.converter.BoToDtoConverter;
import com.epam.epamgymreporter.converter.DtoToBoConverter;
import com.epam.epamgymreporter.epamgymdemo.messaging.TrainingSummaryProducer;
import com.epam.epamgymreporter.model.bo.TrainingSummary;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.repository.TrainingSummaryRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingSummaryServiceTest {

    @Mock
    private TrainingSummaryRepository trainingSummaryRepository;

    @Mock
    private TrainingSummaryProducer trainingSummaryProducer;

    @Mock
    private BoToDtoConverter boToDtoConverter;

    @Mock
    private DtoToBoConverter dtoToBoConverter;

    @InjectMocks
    private TrainingSummaryService trainingSummaryService;

    private TrainingSummary summary;

    private String username;

    private TrainingDto trainingDto;

    @BeforeEach
    public void setUp() {
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

        verify(trainingSummaryRepository, times(1)).save(summary);
        assertEquals(Objects.requireNonNull(trainingSummaryRepository.findByUsername(username).orElse(null))
                .getUsername(), username);
    }

    @Test
    void testCreatingTrainingSummary() {
        when(trainingSummaryRepository.findByUsername(trainingDto.getUsername())).thenReturn(Optional.empty());
        when(dtoToBoConverter.trainingDtoToSummary(trainingDto)).thenReturn(summary);

        trainingSummaryService.saveTrainingSummary(trainingDto);

        verify(trainingSummaryRepository, times(1)).save(summary);
    }

    @Test
    void testSendTrainingSummary() {
        summary.setUsername(username);
        TrainingSummaryDto trainingSummaryDto = TrainingSummaryDto.builder().username(username).build();

        when(trainingSummaryRepository.findByUsername(username)).thenReturn(Optional.of(summary));
        when(boToDtoConverter.summaryToSummaryDto(summary)).thenReturn(trainingSummaryDto);

        trainingSummaryService.sendTrainingSummary(username);

        verify(trainingSummaryProducer, times(1)).send(any());
    }
}
