package com.epam.epamgymreporter;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymreporter.model")
@ComponentScan(basePackages = {"com.epam.epamgymreporter.controller", "com.epam.epamgymreporter.service",
        "com.epam.epamgymreporter.repository", "com.epam.epamgymreporter.aspect", "com.epam.epamgymreporter.config",
        "com.epam.epamgymreporter.exception", "com.epam.epamgymreporter.filter"})
@Generated
public class EpamGymReporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymReporterApplication.class, args);
    }

}
