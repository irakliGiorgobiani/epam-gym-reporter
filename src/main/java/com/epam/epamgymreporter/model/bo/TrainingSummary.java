package com.epam.epamgymreporter.model.bo;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collation = "training-summary")
@CompoundIndex(def = "{'firstName': 1, 'lastName': 1}")
public class TrainingSummary {

    @Id
    private ObjectId id;

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    private Boolean status;

    @NotNull
    private Map<Integer, Map<Integer, Number>> yearlySummary;
}
