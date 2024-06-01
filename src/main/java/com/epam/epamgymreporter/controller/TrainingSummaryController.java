package com.epam.epamgymreporter.controller;

import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.model.dto.TrainingSummaryDto;
import com.epam.epamgymreporter.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/training-summary/v1")
@RequiredArgsConstructor
@Tag(name = "Training Summary",
        description = "Scheduling trainings for trainers and get monthly summary of trainings for each trainer")
public class TrainingSummaryController {

    private final TrainingService trainingService;

    @PostMapping
    @Operation(summary = "Schedule a training for a trainer",
            description = "When crating a training in the main microservice, here we will save it and schedule it" +
                    ", so we can check statistics for individual trainers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a training"),
            @ApiResponse(responseCode = "400", description = "One of the fields is missing")
    })
    public ResponseEntity<String> saveTraining(@RequestBody TrainingDto trainingDto) {
            trainingService.saveTraining(trainingDto);
            return ResponseEntity.ok("Changes have been saved");
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get a monthly summary report of a trainer",
            description = "Get a monthly summary report for an individual trainer by providing the username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned monthly summary"),
            @ApiResponse(responseCode = "400", description = "Trainer does not exist or " +
                    "does not have a training scheduled")
    })
    public ResponseEntity<TrainingSummaryDto> getMonthlyTrainingSummary
            (@PathVariable(name = "username") String username) {
        return ResponseEntity.ok(trainingService.getMonthlyTrainingSummary(username));
    }
}
