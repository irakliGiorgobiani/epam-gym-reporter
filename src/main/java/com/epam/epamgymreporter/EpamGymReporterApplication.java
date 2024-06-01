package com.epam.epamgymreporter;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.epamgymreporter.model")
@Generated
public class EpamGymReporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpamGymReporterApplication.class, args);
    }

}
