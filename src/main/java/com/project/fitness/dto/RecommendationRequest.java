package com.project.fitness.dto;

import com.project.fitness.modal.RecommendationScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationRequest {
    private String userId;
    private String activityId;
    private String type;
    private RecommendationScope scope;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private SafetyResponse safety;
}
