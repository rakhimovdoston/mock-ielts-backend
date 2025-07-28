package com.search.teacher.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.model.entities.RequestLog;
import com.search.teacher.model.response.AIResponse;
import com.search.teacher.repository.RequestLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;
    @Value("${spring.ai.openai.model}")
    private String model;
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    private final RequestLogRepository requestLogRepository;
    private final RestTemplate restTemplate;

    public AIService(ObjectMapper objectMapper, RequestLogRepository requestLogRepository, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.requestLogRepository = requestLogRepository;
        this.restTemplate = restTemplate;
    }

    public AIResponse<JsonNode> getOverallReply(String taskOne, String taskTwo, String taskOneUserAnswer, String taskTwoUserAnswer, String image) {
        String url = "v1/chat/completions";
        long start = System.currentTimeMillis();
        String userRequest = getFullPrompt(taskOne, taskTwo, taskOneUserAnswer, taskTwoUserAnswer, image);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 0.7);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", getInstruction()));
        messages.add(Map.of("role", "user", "content", userRequest));
        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        JsonNode response = null;
        JsonNode errorJson = null;
        String errorMessage = "";
        String status = "error";
        int code = 0;

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.POST,
                    entity,
                    JsonNode.class);

            JsonNode responseBody = responseEntity.getBody();

            assert responseBody != null;
            if (responseBody.has("choices") && responseBody.get("choices").isArray() && !responseBody.get("choices").isEmpty()) {
                response = responseBody.get("choices").get(0).get("message").get("content");
                status = "success";
                code = 200;
            }
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
            errorJson = errorJson(responseBody);
            errorMessage = parseOpenAiErrorMessage(errorJson);
            code = ex.getStatusCode().value();
            status = "error";
            log.error("OpenAI API error: {}", errorMessage);
        } catch (Exception e) {
            errorJson = objectMapper.createObjectNode();
            errorMessage = "Error with AI service";
            code = 408;
            log.error("Error with AI service: error: {}, AI URL: {}", e.getMessage(), url);
        } finally {
            long duration = System.currentTimeMillis() - start;
            requestLogRepository.save(new RequestLog(url, "POST", userRequest, response != null ? response.asText() : errorJson != null ? errorJson.asText() : "", duration, status, errorMessage, code));
        }
        return new AIResponse<>(response, errorMessage, status, code, errorJson);
    }

    private String getInstruction() {
        return """
                I want you to act as a certified IELTS examiner and assess my writing task response as if you were marking a real IELTS test.
                
                For the task I provide (either Task 1 or Task 2), please do the following:
                
                1. Provide an estimated band score (0–9) for each IELTS Writing criterion:
                   - Task Achievement (for Task 1) / Task Response (for Task 2)
                   - Coherence and Cohesion
                   - Lexical Resource
                   - Grammatical Range and Accuracy
                
                2. For **each** criterion, write a clear explanation of the band score (“reason”) and include the following:
                
                   ✅ Exactly **2 or 3** specific examples of a mistake, inaccuracy, or weakness taken from the writing.\s
                   ❌ Do not give fewer than 2 examples unless there is absolutely no more mistake to mention.
                
                   ✅ Use the following strict structure for each mistake:
                       - Mistake #X: In sentence X, you wrote "..."
                       - Explain clearly **why this is wrong** (grammar, cohesion, word choice, etc.)
                       - ✅ Improved version: "..."
                
                   ✅ Also mention **at least one strength** relevant to the criterion if present.
                
                3. Provide **actionable suggestions** for improvement based on the mistakes.
                
                4. Calculate the **overall band score** as the average of the four criteria.
                
                5. Return the entire feedback in this **strict JSON format** (no extra explanations, only JSON):
                
                {
                  "task_1": {
                    "task_achievement": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [
                           {
                             "mistake": "Mistake #1: In sentence X, you wrote \\"...\\"",
                             "explanation": "This is incorrect because ...",
                             "improved_version": "✅ Improved version: \\"...\\""
                           },
                           {
                             "mistake": "Mistake #2: In sentence X, you wrote \\"...\\"",
                             "explanation": "This is incorrect because ...",
                             "improved_version": "✅ Improved version: \\"...\\""
                           },
                           {
                             "mistake": "Mistake #3: In sentence X, you wrote \\"...\\"",
                             "explanation": "This is incorrect because ...",
                             "improved_version": "✅ Improved version: \\"...\\""
                           }
                         ]
                    },
                    "coherence_and_cohesion": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [ ... ]
                    },
                    "lexical_resource": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [ ... ]
                    },
                    "grammatical_range_and_accuracy": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [ ... ]
                    },
                    "overall_band": X.X,
                    "summary": "Short and clear summary of what to improve most urgently."
                  },
                  "task_2": {
                    "task_response": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [
                           {
                             "mistake": "Mistake #1: In sentence X, you wrote \\"...\\"",
                             "explanation": "This is incorrect because ...",
                             "improved_version": "✅ Improved version: \\"...\\""
                           },
                           {
                             "mistake": "Mistake #2: In sentence X, you wrote \\"...\\"",
                             "explanation": "This is incorrect because ...",
                             "improved_version": "✅ Improved version: \\"...\\""
                           },
                           {
                             "mistake": "Mistake #3: In sentence X, you wrote \\"...\\"",
                             "explanation": "This is incorrect because ...",
                             "improved_version": "✅ Improved version: \\"...\\""
                           }
                         ]
                    },
                    "coherence_and_cohesion": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [ ... ]
                    },
                    "lexical_resource": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [ ... ]
                    },
                    "grammatical_range_and_accuracy": {
                      "score": X,
                      "reason": "...",
                      "mistakes": [ ... ]
                    },
                    "overall_band": X.X,
                    "summary": "Short and clear summary of what to improve most urgently."
                  }
                }
                """;
    }

    private String getFullPrompt(String taskOne, String taskTwo, String taskOneUserAnswer, String taskTwoUserAnswer, String image) {
        return """                
                Here is the writing task and my response:
                Task 1 topic: %s
                
                Task 1 Image URL: %s
                
                Task 1 User Answer: %s
                
                Task 2 topic: %s
                
                Task 2 User Answer: %s
                """.formatted(taskOne, image, taskOneUserAnswer, taskTwo, taskTwoUserAnswer);
    }

    private String parseOpenAiErrorMessage(JsonNode errorJson) {
        try {
            return errorJson.has("error") ? errorJson.get("error").get("message").asText() : "Noma'lum AI xatosi";
        } catch (Exception e) {
            log.error("Error parsing AI error response: {}", e.getMessage());
            return "AI response not parsed";
        }
    }

    private JsonNode errorJson(String responseBody) {
        try {
            return objectMapper.readTree(responseBody);
        } catch (Exception e) {
            log.error("Error parsing AI response: {}", e.getMessage());
            return objectMapper.createObjectNode();
        }
    }
}
