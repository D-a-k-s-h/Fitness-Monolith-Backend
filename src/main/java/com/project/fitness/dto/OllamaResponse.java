package com.project.fitness.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OllamaResponse {
    private String model;
    private String response;
    private boolean done;
    private String thinking;
    @JsonProperty("created_at")
    private String createdAt;
}
