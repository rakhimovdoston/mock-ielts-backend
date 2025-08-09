package com.search.teacher.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.io.IOException;
import java.util.Base64;
import java.net.URL;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

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

    private String getImageMimeType(String imageUrl) {
        if (imageUrl == null) return "image/png"; // default
        imageUrl = imageUrl.toLowerCase();
        if (imageUrl.endsWith(".png")) return "image/png";
        if (imageUrl.endsWith(".jpg") || imageUrl.endsWith(".jpeg")) return "image/jpeg";
        return "image/png";
    }

    public String toBase64DataUrl(String imageUrl) {
        String mimeType = getImageMimeType(imageUrl);
        try (InputStream is = new URL(imageUrl).openStream()) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();

            byte[] imageBytes = os.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            return "data:" + mimeType + ";base64," + base64;
        } catch (IOException e) {
            log.error("Error converting image to base64 data url: {}", e.getMessage());
            return null;
        }
    }

    public String getDescriptionImage(String image) {
        String baseImage = toBase64DataUrl(image);
        if (baseImage == null) {
            return null;
        }
        String url = "v1/chat/completions";
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // 🔒 OpenAI API key

        // JSON body yaratish
//        strong suggest "Describe the visual data in this image exactly as it would appear in an IELTS Academic Writing Task 1 prompt. Use precise and formal language. If any data is unclear or missing, do not speculate. Focus only on what is objectively visible and relevant for academic report writing."
        String prompt = """
                {
                  "model": "gpt-4o",
                  "messages": [
                    {
                      "role": "user",
                      "content": [
                        {
                          "type": "text",
                          "text": "Describe the visual data in this image exactly as it would appear in an IELTS Academic Writing Task 1 prompt. Use precise and formal language. If any data is unclear or missing, do not speculate. Focus only on what is objectively visible and relevant for academic report writing."
                        },
                        {
                          "type": "image_url",
                          "image_url": {
                            "url": "%s"
                          }
                        }
                      ]
                    }
                  ],
                  "max_tokens": 1000
                }
                """;
        String body = prompt.formatted(baseImage);

        String response = null;
        String errorJson = null;
        String errorMessage = "";
        String status = "error";
        int code = 0;

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<Map> responseAPI = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            // JSON ichidan javobni ajratish
            Map<String, Object> result = responseAPI.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) result.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            response = (String) message.get("content");
            status = "success";
            code = 200;
        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            errorJson = ex.getResponseBodyAsString();
            errorMessage = "Image analysis failed.";
            code = ex.getStatusCode().value();
            status = "error";
            log.error("OpenAI API Image analysis error: {}", errorMessage);
        } catch (Exception e) {
            code = 408;
            e.printStackTrace();
            status = "ai_error";
            errorMessage = "Error with AI service";
            log.error("Error with AI service image analysis: error: {}, AI URL: {}", e.getMessage(), url);
        } finally {
            long duration = System.currentTimeMillis() - start;
            requestLogRepository.save(new RequestLog(url, "POST", prompt.formatted(image), response != null ? response : errorJson, duration, status, errorMessage, code, "image_analysis"));
        }

        return response;
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

    public AIResponse<JsonNode> checkWriting(String answer, String topic, String image, boolean taskOne) {
        String url = "v1/chat/completions";
        long start = System.currentTimeMillis();
        String systemPrompt = taskOne ? getPromptTaskOne() : getPromptTaskTwo();

        String userRequest;
        if (taskOne) {
            userRequest = """
                    {
                      "task_type": "task_1",
                      "visual_description": "%s",
                      "essay": "%s"
                    }
                    """.formatted(image, answer);
        } else {
            userRequest = """
                    {
                      "task_type": "task_2",
                      "question_prompt": "%s",
                      "essay": "%s"
                    }
                    """.formatted(topic, answer);
        }
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 0.1);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userRequest));
        requestBody.put("messages", messages);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        JsonNode response = null;
        JsonNode errorJson = null;
        String errorMessage = "";
        String status = "error";
        int code = 0;
        try {
            ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
                    baseUrl + url,
                    HttpMethod.POST,
                    entity,
                    JsonNode.class);

            response = responseEntity.getBody();
            assert response != null;
            if (response.has("choices") && response.get("choices").isArray() && !response.get("choices").isEmpty()) {
                String responseStr = response.get("choices").get(0).get("message").get("content").asText();
                String cleanedContent = cleanJsonBlock(responseStr);
                response = objectMapper.readTree(cleanedContent);
                status = "success";
                code = 200;
            } else {
                errorMessage = "Error with AI service";
                code = responseEntity.getStatusCode().value();
                log.error("Error with AI service: error: {}, AI URL: {}, Status code: {}", "No choices", url, code);
            }

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            String responseBody = ex.getResponseBodyAsString();
            errorJson = errorJson(responseBody);
            errorMessage = parseOpenAiErrorMessage(errorJson);
            code = ex.getStatusCode().value();
            status = "error";
            log.error("OpenAI API error: {}, task: {}", errorMessage, taskOne ? "Task 1" : "Task 2");
        } catch (Exception e) {
            errorJson = objectMapper.createObjectNode();
            errorMessage = "Error with AI service";
            code = 408;
            log.error("Error with AI service: error: {}, AI URL: {}", e.getMessage(), url);
        } finally {
            long duration = System.currentTimeMillis() - start;
            requestLogRepository.save(new RequestLog(url, "POST", userRequest, response != null ? response.toString() : errorJson != null ? errorJson.asText() : "", duration, status, errorMessage, code, taskOne ? "ai_task_1" : "ai_task_2"));
        }
        return new AIResponse<>(response, errorMessage, status, code, errorJson);
    }

    private String getPromptTaskOne() {

        return """
                You are an IELTS examiner with deep expertise in IELTS Writing Task 1 assessment according to the official IELTS band descriptors.
                
                You will be given two inputs:
                1. The description of the visual information (e.g., chart, graph, diagram, process, table, or image) that the student was supposed to write about.
                2. The student's essay.
                
                Your task:
                1. Read the visual description carefully.
                2. Read the student's essay carefully.
                3. Evaluate the essay strictly according to IELTS Writing Task 1 band descriptors:
                   - Task Achievement (how well the student described the visual data, covered key features, and avoided irrelevant information)
                   - Coherence and Cohesion (logical organization, paragraphing, cohesive devices)
                   - Lexical Resource (range and appropriacy of vocabulary)
                   - Grammatical Range and Accuracy (variety and correctness of grammar)
                4. For each criterion:
                   - Assign an **integer score** from 0 to 9.
                   - Provide a **reason** explaining the score, directly tied to IELTS descriptors and the visual description requirements.
                   - Give a **practical suggestion** for improvement.
                   - Highlight one **strength** the student demonstrated.
                   - Provide **1–3 mistakes** in this structure:
                       {
                         "mistake": "Original sentence...",
                         "explanation": "Why it is wrong...",
                         "improved_version": "✅ Corrected sentence..."
                       }
                   - Choose a **relevant sticker emoji** representing the criterion (e.g., 💪, 📚, ✍️, 🔧).
                5. Calculate the **overall_band** (average of the four criteria, rounded to the nearest half-band).
                6. Write a **summary** highlighting both strengths and weaknesses.
                7. Give a short, **uplifting encouragement** that motivates the student.
                8. Provide **3 stickers** in a "stickers" array, representing the overall feedback vibe.
                
                Output Rules:
                - Respond ONLY in valid JSON matching the exact structure below.
                - Do NOT add any extra text or explanations outside of the JSON.
                - Keep language clear, precise, and human-friendly.
                
                Output JSON structure:
                {
                  "task_achievement": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [
                      {
                        "mistake": "Original sentence...",
                        "explanation": "Why it's wrong...",
                        "improved_version": "✅ Corrected sentence..."
                      }
                    ],
                    "sticker": "💪"
                  },
                  "coherence_and_cohesion": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [
                      {
                        "mistake": "Original sentence...",
                        "explanation": "Why it's wrong...",
                        "improved_version": "✅ Corrected sentence..."
                      }
                    ],
                    "sticker": "📚"
                  },
                  "lexical_resource": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [
                      {
                        "mistake": "Original sentence...",
                        "explanation": "Why it's wrong...",
                        "improved_version": "✅ Corrected sentence..."
                      }
                    ],
                    "sticker": "✍️"
                  },
                  "grammatical_range_and_accuracy": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [
                      {
                        "mistake": "Original sentence...",
                        "explanation": "Why it's wrong...",
                        "improved_version": "✅ Corrected sentence..."
                      }
                    ],
                    "sticker": "🔧"
                  },
                  "overall_band": X.X,
                  "summary": "Brief summary highlighting strengths and weaknesses.",
                  "encouragement": "Positive and uplifting comment tailored to the student's effort and current performance.",
                  "stickers": ["🔥", "📚", "💪"]
                }
                """;
    }

    private String getPromptTaskTwo() {
        return """
                You are an IELTS examiner with deep expertise in IELTS Writing Task 2 assessment according to the official IELTS band descriptors.
                
                You will be given two inputs:
                1. The exact IELTS Writing Task 2 question prompt.
                2. The student's essay.
                
                Your task:
                1. Read the question prompt carefully to understand the topic and the requirements (e.g., discuss both views, give your own opinion, advantages/disadvantages, problem/solution).
                2. Read the student's essay carefully.
                3. Evaluate the essay strictly according to IELTS Writing Task 2 band descriptors:
                  - Task Response (how well the student addressed all parts of the question, presented a clear position, supported ideas with evidence/examples, and developed arguments fully)
                  - Coherence and Cohesion (logical flow, paragraphing, and use of cohesive devices)
                  - Lexical Resource (range, accuracy, and appropriacy of vocabulary)
                  - Grammatical Range and Accuracy (variety and correctness of grammar)
                4. For each criterion:
                  - Assign an **integer score** from 0 to 9.
                  - Provide a **reason** explaining the score, directly tied to IELTS descriptors and the question requirements.
                  - Give a **practical suggestion** for improvement.
                  - Highlight one **strength** the student demonstrated.
                  - Provide **1–3 mistakes** in this structure:
                      {
                        "mistake": "Original sentence...",
                        "explanation": "Why it is wrong...",
                        "improved_version": "✅ Corrected sentence..."
                      }
                  - Choose a **relevant sticker emoji** representing the criterion (e.g., 💪, 📚, ✍️, 🔧).
                5. Calculate the **overall_band** (average of the four criteria, rounded to the nearest half-band).
                6. Write a **summary** highlighting both strengths and weaknesses.
                7. Give a short, **uplifting encouragement** that motivates the student.
                8. Provide **3 stickers** in a "stickers" array, representing the overall feedback vibe.
                
                Output Rules:
                - Respond ONLY in valid JSON matching the exact structure below.
                - Do NOT add any extra text or explanations outside of the JSON.
                - Keep language clear, precise, and human-friendly.
                
                Output JSON structure:
                {
                 "task_response": {
                   "score": X,
                   "reason": "...",
                   "suggestion": "...",
                   "strength": "...",
                   "mistakes": [
                     {
                       "mistake": "Original sentence...",
                       "explanation": "Why it's wrong...",
                       "improved_version": "✅ Corrected sentence..."
                     }
                   ],
                   "sticker": "💪"
                 },
                 "coherence_and_cohesion": {
                   "score": X,
                   "reason": "...",
                   "suggestion": "...",
                   "strength": "...",
                   "mistakes": [
                     {
                       "mistake": "Original sentence...",
                       "explanation": "Why it's wrong...",
                       "improved_version": "✅ Corrected sentence..."
                     }
                   ],
                   "sticker": "📚"
                 },
                 "lexical_resource": {
                   "score": X,
                   "reason": "...",
                   "suggestion": "...",
                   "strength": "...",
                   "mistakes": [
                     {
                       "mistake": "Original sentence...",
                       "explanation": "Why it's wrong...",
                       "improved_version": "✅ Corrected sentence..."
                     }
                   ],
                   "sticker": "✍️"
                 },
                 "grammatical_range_and_accuracy": {
                   "score": X,
                   "reason": "...",
                   "suggestion": "...",
                   "strength": "...",
                   "mistakes": [
                     {
                       "mistake": "Original sentence...",
                       "explanation": "Why it's wrong...",
                       "improved_version": "✅ Corrected sentence..."
                     }
                   ],
                   "sticker": "🔧"
                 },
                 "overall_band": X.X,
                 "summary": "Brief summary highlighting strengths and weaknesses.",
                 "encouragement": "Positive and uplifting comment tailored to the student's effort and current performance.",
                 "stickers": ["🔥", "📚", "💪"]
                }
                """;
    }

    private String cleanJsonBlock(String raw) {
        if (raw.startsWith("```json")) {
            return raw.replaceAll("(?s)^```json\\s*|\\s*```$", "").trim();
        } else if (raw.startsWith("```")) {
            return raw.replaceAll("(?s)^```\\s*|\\s*```$", "").trim();
        }
        return raw.trim();
    }
}
