package com.project.fitness.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudChatRequest {
    private String model;
    private List<CloudChatMessage> messages;
    private boolean stream;

    @JsonProperty("response_format")
    private Map<String, String> responseFormat;
}
