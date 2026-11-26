package com.project.fitness.controller;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/generate")
    public ResponseEntity<?> generateRecommendation(@RequestBody RecommendationRequest request) {
        return recommendationService.generateRecommendation(request.getActivityId(), request.getUserId());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserRecommendation(@PathVariable(name = "userId") String userId) {
        return recommendationService.getUserRecommendation(userId);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<?> getActivityRecommendation(@PathVariable(name = "activityId") String activityId) {
        return recommendationService.getActivityRecommendation(activityId);
    }
}
