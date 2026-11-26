package com.project.fitness.configuration;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RecommendationSchema {
    public Map<String, Object> rootSchema() {
        Map<String, Object> schema = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put("type", Map.of("type", "string"));

        properties.put("recommendation", Map.of("type", "string"));

        Map<String, Object> improvements = new HashMap<>();
        improvements.put("type", "array");
        improvements.put("items", Map.of("type", "string"));
        properties.put("improvements", improvements);

        Map<String, Object> suggestions = new HashMap<>();
        suggestions.put("type", "array");
        suggestions.put("items", Map.of("type", "string"));
        properties.put("suggestions", suggestions);

        Map<String, Object> safety = new HashMap<>();
        Map<String, Object> safetyProperties = new HashMap<>();
        safety.put("type", "object");
        safety.put("additionalProperties", false);
        safetyProperties.put("level", Map.of("type", "string"));
        safetyProperties.put("message", Map.of("type", "string"));
        safety.put("properties", safetyProperties);
        safety.put("required", List.of("level", "message"));
        properties.put("safety", safety);

        schema.put("type", "object");
        schema.put("additionalProperties", false);
        schema.put("properties", properties);
        schema.put("required", List.of(
                "type",
                "recommendation",
                "improvements",
                "suggestions",
                "safety"
        ));

        return schema;
    }
}
