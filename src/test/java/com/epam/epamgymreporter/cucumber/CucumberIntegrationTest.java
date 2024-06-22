package com.epam.epamgymreporter.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/resources/features", glue = "com.epam.epamgymreporter.cucumber.steps")
public class CucumberIntegrationTest {
}
