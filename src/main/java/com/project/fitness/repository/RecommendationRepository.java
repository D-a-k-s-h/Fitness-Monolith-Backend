package com.project.fitness.repository;

import com.project.fitness.modal.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, String> {
    Recommendation findByUserId(String userId);

    Recommendation findByActivityId(String activityId);
}
