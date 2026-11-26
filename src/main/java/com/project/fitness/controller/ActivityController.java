package com.project.fitness.controller;

import com.project.fitness.dto.TrackActivity;
import com.project.fitness.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/trackActivity")
    public ResponseEntity<?> trackActivity(@RequestBody TrackActivity activity) {
        return activityService.trackActivity(activity);
    }

    //@PreAuthorize("hasRole('USER')")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/retrieveActivity")
    public ResponseEntity<?> getUserActivities(@RequestHeader(value = "X-User-ID") String userId) {
        return activityService.retrieveActivity(userId);
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<?> getActivity(@PathVariable(name = "activityId") String activityId) {
        return activityService.getActivity(activityId);
    }

    @DeleteMapping("/deleteActivity/{activityId}")
    public ResponseEntity<?> deleteActivity(@PathVariable(name = "activityId") String activityId) {
        return activityService.deleteActivity(activityId);
    }
}
