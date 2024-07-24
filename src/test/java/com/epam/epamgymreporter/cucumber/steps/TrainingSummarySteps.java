package com.epam.epamgymreporter.cucumber.steps;

import com.epam.epamgymreporter.model.bo.TrainingSummary;
import com.epam.epamgymreporter.model.dto.TrainingDto;
import com.epam.epamgymreporter.repository.TrainingSummaryRepository;
import com.epam.epamgymreporter.service.TrainingSummaryService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@CucumberContextConfiguration
@SpringBootTest
@RequiredArgsConstructor
public class TrainingSummarySteps {

    private final TrainingSummaryService trainingSummaryService;

    private final TrainingSummaryRepository trainingSummaryRepository;

    private TrainingDto trainingDto;

    @Given("a new TrainingDto instance with the username {string}")
    public void a_new_training_instance_with_the_username(String username) {
        trainingDto = TrainingDto.builder()
                .username(username)
                .firstName("brad")
                .lastName("andersen")
                .status(true)
                .trainingDate(LocalDate.now())
                .trainingDuration(2)
                .build();
    }

    @When("saving to the database")
    public void saving_to_the_database() {
        trainingSummaryService.saveTrainingSummary(trainingDto);
    }

    @Then("a new training summary is added to the database with the username {string}")
    public void a_new_training_summary_is_added_to_the_database(String username) {
        assertEquals(Objects.requireNonNull(trainingSummaryRepository.findByUsername(username).orElse(null))
                        .getUsername(),
                trainingDto.getUsername());
    }

    @And("existing training summary instance with the username {string}")
    public void existing_training_summary_instance_with_the_username(String username) {
        TrainingSummary trainingSummary = TrainingSummary.builder()
                .username(username)
                .firstName("john")
                .lastName("doe")
                .status(true)
                .yearlySummary(new HashMap<>())
                .build();
        trainingSummaryRepository.save(trainingSummary);
    }

    @Then("the training summary with the username {string} is updated")
    public void the_training_summary_with_the_same_username_is_updated(String username) {
        assertEquals(Objects.requireNonNull(trainingSummaryRepository.findByUsername(username).orElse(null))
                .getFirstName(),
                trainingDto.getFirstName());
    }
}
