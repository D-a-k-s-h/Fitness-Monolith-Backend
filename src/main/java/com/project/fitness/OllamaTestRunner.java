//package com.project.fitness;
//
//import com.project.fitness.configuration.RecommendationSchema;
//import com.project.fitness.dto.GeneratedRecommendation;
//import com.project.fitness.dto.OllamaRequest;
//import com.project.fitness.dto.OllamaResponse;
//import com.project.fitness.service.OllamaService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import tools.jackson.databind.ObjectMapper;
//
//@Component
//@RequiredArgsConstructor
//public class OllamaTestRunner implements CommandLineRunner {
//    private final OllamaService ollamaService;
//    private final RecommendationSchema recommendationSchema;
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public void run(String... args) throws Exception {
//        OllamaRequest ollamaRequest = new OllamaRequest(
//                "qwen3:1.7b",
//                "Give me three simple fitness habits for a beginner. type must be one of PERFORMANCE, FORM_IMPROVEMENT, RECOVERY, ENDURANCE, SAFETY, or GENERAL. safety.level must be one of LOW, MEDIUM, or HIGH. safety.message must contain only relevant safety advice. recommendation must contain the main recommendation, not a repetition of the other fields. Return only the JSON object matching the provided schema. improvements and suggestions must each be arrays of strings containing actionable fitness advice.",
//                false, recommendationSchema.rootSchema());
//
//        OllamaResponse response = ollamaService.generateResponse(ollamaRequest);
//
//        GeneratedRecommendation generate = objectMapper.readValue(response.getResponse(), GeneratedRecommendation.class);
//
//        System.out.println("Type: " + generate.getType());
//        System.out.println("Recommendation: " + generate.getRecommendation());
//        System.out.println("Safety level: " + generate.getSafety().getLevel());
//        System.out.println("Safety message: " + generate.getSafety().getMessage());
//    }
//}
