package com.project.fitness.service;

import com.project.fitness.dto.GeneratedRecommendation;
import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.modal.Activity;
import com.project.fitness.modal.Recommendation;
import com.project.fitness.modal.RecommendationScope;
import com.project.fitness.modal.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final OllamaService ollamaService;

    public ResponseEntity<?> generateRecommendation(String activityId, String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found by id: " + userId));
            Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("No activity found by id: " + activityId));

            String prompt = """
                    Generate a personalized fitness recommendation based on this activity
                    and use the activity's actual values rather than inventing or modifying them.
                    Try to keep recommendation and safety message under 255 characters.
                    
                    Activity type: %s
                    Duration in minutes: %s
                    Calories burned: %s
                    Additional metrics: %s
                    
                    Return a recommendation using the provided JSON schema.
                    """.formatted(
                    activity.getType(),
                    activity.getDuration(),
                    activity.getCaloriesBurned(),
                    activity.getAdditionalMetrics()
            );

            GeneratedRecommendation generatedRecommendation = ollamaService.generateResponse(prompt);

            Recommendation recommendation = Recommendation.builder()
                    .suggestions(generatedRecommendation.getSuggestions())
                    .safety(generatedRecommendation.getSafety())
                    .user(user)
                    .activity(activity)
                    .recommendation(generatedRecommendation.getRecommendation())
                    .type(generatedRecommendation.getType())
                    .scope(RecommendationScope.ACTIVITY)
                    .improvements(generatedRecommendation.getImprovements())
                    .build();

            Recommendation savedRecommendation = recommendationRepository.save(recommendation);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedRecommendation));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    private RecommendationResponse mapToResponse(Recommendation recommendation) {
        return new RecommendationResponse(
                recommendation.getId(),
                recommendation.getUser().getId(),
                recommendation.getActivity().getId(),
                recommendation.getType(),
                recommendation.getScope(),
                recommendation.getRecommendation(),
                recommendation.getImprovements(),
                recommendation.getSuggestions(),
                recommendation.getSafety()
        );
    }

    public ResponseEntity<?> getUserRecommendation(String userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("No user found with id: " + userId));

            List<Recommendation> userList = user.getRecommendations();
            if (userList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userList);
            }

            return ResponseEntity.status(HttpStatus.OK).body(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public ResponseEntity<?> getActivityRecommendation(String activityId) {
        try {
            Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("No activity found with id: " + activityId));

            List<Recommendation> activityList = activity.getRecommendations();
            if (activityList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(activityList);
            }

            return ResponseEntity.status(HttpStatus.OK).body(activityList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public void deleteRecommendation(Recommendation rec) {
        Recommendation recommendation = recommendationRepository.findById(rec.getId()).orElseThrow(() -> new RuntimeException("No recommendation found by id: " + rec.getId()));
        recommendationRepository.delete(recommendation);
    }
}
