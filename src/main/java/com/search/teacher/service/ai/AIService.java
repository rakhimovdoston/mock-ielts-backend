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
            requestLogRepository.save(new RequestLog(url, "POST", userRequest, response != null ? response.asText() : errorJson != null ? errorJson.asText() : "", duration, status, errorMessage, code, "ai_overall_reply"));
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

    public AIResponse<JsonNode> checkWriting(String answer, String topic, String image, boolean taskOne) {
        String url = "v1/chat/completions";
        long start = System.currentTimeMillis();
        String userRequest = taskOne ? getPromptTaskOne(answer, image) : getPromptTaskTwo(topic, answer);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 0.3);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are a certified IELTS Writing examiner."));
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

    private String getPromptTaskOne(String answer, String image) {
        return """
                Please assess the following Writing Task 1 essay based on the official IELTS Band Descriptors.
                
                Return your evaluation as a **strictly formatted JSON** object with the following structure:
                
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
                      },
                      ...
                    ],
                    "sticker": "💪"
                  },
                  "coherence_and_cohesion": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [ ... ],
                    "sticker": "📚"
                  },
                  "lexical_resource": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [ ... ],
                    "sticker": "✍️"
                  },
                  "grammatical_range_and_accuracy": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [ ... ],
                    "sticker": "🔧"
                  },
                  "overall_band": X.X,
                  "summary": "Brief summary highlighting strengths and weaknesses.",
                  "encouragement": "Positive and uplifting comment tailored to the student's effort and current performance.",
                  "stickers": ["🔥", "📚", "💪"]
                }
                
                💡 Evaluation rules:
                - Provide **a whole band score (1–9)** per criterion.
                - `overall_band` must be the **average** of the 4 scores, rounded to the **nearest 0.5**.
                - You must identify **at least one mistake per category**, and **at least 3 total mistakes**.
                - If possible, return **up to 2 or 3 mistakes per category**, not just 1.
                - Each mistake must include:
                  - The original sentence.
                  - An explanation of the error.
                  - A corrected version.
                - Mistakes should be **meaningful**, affecting clarity, accuracy, or appropriateness.
                - In **each of the 4 criteria**, also include:
                  - `"strength"`: A sentence describing what the student did well.
                  - `"sticker"`: A supportive emoji that reflects performance (e.g., 🔥, 📚, 💪, ✍️, 🚀, 🔧, ⏳).
                - Also include:
                  - `"encouragement"`: A kind, uplifting message tailored to the student’s current performance.
                  - `"stickers"`: A list of 1–3 emojis representing the tone of the full review. These should feel motivational and sincere.
                
                📝 Task:
                You will now assess a student's Task 1 essay, based on the provided visual chart description (image analysis) and their response.
                
                --- Task 1 Visual Description ---
                %s
                
                --- Student Essay ---
                %s
                
                Return only the JSON object. Do not include extra explanations.
                """.formatted(image, answer);
    }

    private String getPromptTaskTwo(String topic, String answer) {
        return """
                Please assess the following IELTS Writing Task 2 essay using the official IELTS Band Descriptors.
                
                Please return a STRICTLY formatted JSON object with this structure:
                
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
                    "sticker": "🎯"
                  },
                  "coherence_and_cohesion": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [ ... ],
                    "sticker": "🔗"
                  },
                  "lexical_resource": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [ ... ],
                    "sticker": "🧠"
                  },
                  "grammatical_range_and_accuracy": {
                    "score": X,
                    "reason": "...",
                    "suggestion": "...",
                    "strength": "...",
                    "mistakes": [ ... ],
                    "sticker": "🛠️"
                  },
                  "overall_band": X.X,
                  "summary": "Brief summary of strengths and weaknesses.",
                  "encouragement": "A kind, motivational comment tailored to the student's effort and score.",
                  "stickers": ["🔥", "💪", "🚀"]
                }
                
                📝 Scoring Instructions:
                - Each score must be an INTEGER between 1 and 9.
                - `overall_band` should be the average of all 4 criteria, **rounded to the nearest 0.5** (e.g., 6.25 → 6.5, 7.75 → 8.0).
                - Under each criterion, provide:
                  - `"reason"`: why the score was awarded.
                  - `"suggestion"`: a clear and practical tip to improve.
                  - `"strength"`: what the student did well in that criterion.
                  - `"mistakes"`: an array of 1–3 actual writing errors (don’t invent mistakes).
                    - Each must contain:
                      - `"mistake"`: the original sentence,
                      - `"explanation"`: why it’s wrong,
                      - `"improved_version"`: a corrected version starting with "✅".
                  - `"sticker"`: a supportive emoji (🎯, 🔗, 🧠, 🛠️, ✨, 💡) summarizing performance in that criterion.
                - Outside of the criteria, include:
                  - `"encouragement"`: a brief motivational message.
                  - `"stickers"`: a final set of 1–3 emojis that reflect the overall tone.
                
                ✅ You must ensure the essay directly addresses the provided topic and critically responds to its core ideas.
                
                --- Writing Topic ---
                %s
                
                --- Student Essay ---
                %s
                
                Return only the JSON object. Do not include anything else.
                """.formatted(topic, answer);
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
