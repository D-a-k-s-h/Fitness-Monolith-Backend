package com.project.fitness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedRecommendation {
    private String type;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private SafetyResponse safety;
}
