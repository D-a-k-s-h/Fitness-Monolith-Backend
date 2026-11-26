package com.project.fitness.service;

import com.project.fitness.configuration.RecommendationSchema;
import com.project.fitness.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OllamaService {
    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final RecommendationSchema recommendationSchema;

    @Value("${ai.provider:LOCAL}")
    private String aiProvider;

    @Value("${ai.model:qwen3:1.7b}")
    private String aiModel;

    @Value("${ai.api-key}")
    private String apiKey;

    public OllamaResponse generateResponse(OllamaRequest request) {
        return restClient
                .post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OllamaResponse.class);

    }

    public GeneratedRecommendation generateResponse(String prompt) {

        if ("LOCAL".equalsIgnoreCase(aiProvider)) {
            return generateLocalRecommendation(prompt);
        }

        if ("CLOUD".equalsIgnoreCase(aiProvider)) {
            return generateCloudRecommendation(prompt);
        }

        throw new IllegalStateException(
                "Unsupported AI provider: " + aiProvider
        );
    }

    private GeneratedRecommendation generateCloudRecommendation(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Ollama Cloud API Key is not configured.");
        }

        String systemPrompt = """
                You are a fitness recommendation assistant.
                
                Return ONLY a valid JSON object.
                Do not include markdown or code fences.
                
                The JSON must contain exactly these fields:
                
                {
                    "type": "string",
                    "recommendation": "string",
                    "improvements": ["string"],
                    "suggestions": ["string"],
                    "safety": {
                        "level": "string",
                        "message": "string"
                    }
                }
                
                All fields are required.
                Do not include additional fields.
                """;

        CloudChatRequest request = new CloudChatRequest(
                aiModel,
                List.of(
                        new CloudChatMessage("system", systemPrompt),
                        new CloudChatMessage("user", prompt)
                ),
                false,
                Map.of("type", "json_object")
        );

        CloudChatResponse response = restClient
                .post()
                .uri("/chat/completions")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + apiKey
                )
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CloudChatResponse.class);

        if (response == null || response.getChoices() == null || response.getChoices().isEmpty() || response.getChoices().getFirst().getMessage() == null || response.getChoices().getFirst().getMessage().getContent() == null) {
            throw new IllegalStateException("Ollama Cloud returned an empty response");
        }

        String content = response.getChoices()
                .getFirst()
                .getMessage()
                .getContent();

        return parseRecommendation(content);
    }

    private GeneratedRecommendation generateLocalRecommendation(String prompt) {
        OllamaRequest ollamaRequest = new OllamaRequest(
                "qwen3:1.7b",
                prompt,
                false,
                recommendationSchema.rootSchema()
        );

        OllamaResponse ollamaResponse = generateResponse(ollamaRequest);

        if (ollamaResponse == null || ollamaResponse.getResponse() == null) {
            throw new IllegalStateException("Ollama returned an empty response");
        }

        return parseRecommendation(ollamaResponse.getResponse());
    }

    private GeneratedRecommendation parseRecommendation(String content) {
        GeneratedRecommendation recommendation =
                objectMapper.readValue(content, GeneratedRecommendation.class);

        if (recommendation == null
                || recommendation.getType() == null
                || recommendation.getRecommendation() == null
                || recommendation.getImprovements() == null
                || recommendation.getSuggestions() == null
                || recommendation.getSafety() == null
                || recommendation.getSafety().getLevel() == null
                || recommendation.getSafety().getMessage() == null) {

            throw new IllegalStateException(
                    "AI returned an incomplete recommendation"
            );
        }

        return recommendation;
    }
}
