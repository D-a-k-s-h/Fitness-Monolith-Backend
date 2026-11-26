package com.project.fitness.service;

import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.dto.TrackActivity;
import com.project.fitness.modal.Activity;
import com.project.fitness.modal.Recommendation;
import com.project.fitness.modal.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final RecommendationService recommendationService;

    @Transactional
    public ResponseEntity<?> trackActivity(TrackActivity trackActivity) {
        try {
            User user = userRepository.findById(trackActivity.getUserId()).orElseThrow(() -> new RuntimeException("No user found with id: " + trackActivity.getUserId()));

            Activity activity = Activity.builder()
                    .type(trackActivity.getType())
                    .additionalMetrics(trackActivity.getAdditionalMetrics())
                    .duration(trackActivity.getDuration())
                    .caloriesBurned(trackActivity.getCaloriesBurned())
                    .startTime(trackActivity.getStartTime())
                    .endTime(trackActivity.getEndTime())
                    .user(user)
                    .build();

            Activity trackedActivity = activityRepository.save(activity);

            try {
                recommendationService.generateRecommendation(trackedActivity.getId(), user.getId());
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(400).body("Failed to generate recommendation for activity");
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(mapToActivity(trackedActivity));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    private ActivityResponse mapToActivity(Activity trackedActivity) {
        return new ActivityResponse(
                trackedActivity.getId(),
                trackedActivity.getType(),
                trackedActivity.getAdditionalMetrics(),
                trackedActivity.getUser().getId(),
                trackedActivity.getDuration(),
                trackedActivity.getCaloriesBurned(),
                trackedActivity.getStartTime(),
                trackedActivity.getEndTime(),
                trackedActivity.getCreatedAt(),
                trackedActivity.getUpdatedAt()
        );
    }

    public ResponseEntity<?> retrieveActivity(String userId) {
        try {
            List<ActivityResponse> activityList = activityRepository.findByUserId(userId).stream().map(this::mapToActivity).toList();

            if (activityList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(activityList);
            }

            return ResponseEntity.ok(activityList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public ResponseEntity<?> deleteActivity(String activityId) {
        try {
            Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("No activity found with id: " + activityId));

            List<Recommendation> recommendationList = activity.getRecommendations();

            if (!recommendationList.isEmpty()) {
                recommendationList
                        .forEach(recommendationService::deleteRecommendation);
            }

            activityRepository.delete(activity);

            return ResponseEntity.status(HttpStatus.OK).body("Activity Deleted Successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }

    public ResponseEntity<?> getActivity(String activityId) {
        try {
            Activity activity = activityRepository.findById(activityId).orElseThrow(() -> new RuntimeException("No activity exist with id: " + activityId));

            ActivityResponse response = mapToActivity(activity);

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(e);
        }
    }
}
