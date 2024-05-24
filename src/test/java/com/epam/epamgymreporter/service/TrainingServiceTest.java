package com.epam.epamgymreporter.service;

import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.model.bo.Training;
import com.epam.epamgymreporter.repository.TrainingRepository;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void testSaveTrainingAdd() {
        TrainingDto trainingDto = TrainingDto.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .active(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(1.5)
                .actionType("ADD")
                .build();

        Training training = Training.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .active(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(1.5)
                .build();

        when(trainingRepository.save(any(Training.class))).thenReturn(training);

        trainingService.saveTraining(trainingDto);

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void testSaveTrainingDelete() {
        TrainingDto trainingDto = TrainingDto.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .active(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(1.5)
                .actionType("DELETE")
                .build();

        trainingService.saveTraining(trainingDto);

        verify(trainingRepository, times(1))
                .deleteByUsernameAndTrainingDateAndTrainingDuration(
                eq("username"), eq(LocalDate.now()), eq(1.5)
        );
    }

    @Test
    void testGetMonthlyTrainingSummary() {
        List<Training> trainings = new ArrayList<>();
        trainings.add(Training.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .active(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(1.5)
                .build());

        when(trainingRepository.getTrainingByUsername("username")).thenReturn(trainings);

        TrainingSummaryDto summaryDto = trainingService.getMonthlyTrainingSummary("username");

        assertNotNull(summaryDto);
        assertEquals("username", summaryDto.getUsername());
        assertEquals("firstName", summaryDto.getFirstName());
        assertEquals("lastName", summaryDto.getLastName());
        assertEquals("Active", summaryDto.getStatus());

        Map<Integer, Map<Integer, Double>> expectedYearlySummary = new HashMap<>();
        Map<Integer, Double> monthlySummary = new HashMap<>();
        monthlySummary.put(LocalDate.now().getMonthValue(), 1.5);
        expectedYearlySummary.put(LocalDate.now().getYear(), monthlySummary);

        assertEquals(expectedYearlySummary, summaryDto.getYearlySummary());
    }
//
//    @Test
//    void testGetMonthlyTrainingSummary() {
//        when(trainingRepository.getTrainingByUsername("username")).thenReturn(new ArrayList<>());
//
//        assertThrows(NotFoundException.class, () -> trainingService.getMonthlyTrainingSummary("username"));
//    }
}
