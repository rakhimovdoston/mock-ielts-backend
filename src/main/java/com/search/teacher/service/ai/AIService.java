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
        requestBody.put("temperature", 0.3);

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
                Give a score breakdown according to IELTS writing criteria. Note! You have to be as objective as possible! Analyze the writing and compare the parts to the band descriptors and choose the most appropriate score
                
                Output rules:
                 - Respond ONLY in valid JSON with the exact structure below.
                 - Keep language clear, precise, and friendly.
                
                 Output JSON structure:
                 {
                   "task_achievement": {
                     "score": X.X,
                     "reason": "...",
                     "strength": "...",
                     "suggestion": "...",
                     "mistakes": [
                       {
                         "mistake": "...",
                         "explanation": "...",
                         "improved_version": "✅ ..."
                       }
                     ],
                     "sticker": "💪"
                   },
                   "coherence_and_cohesion": {
                     "score": X.X,
                     "reason": "...",
                     "strength": "...",
                     "suggestion": "...",
                     "mistakes": [],
                     "sticker": "📚"
                   },
                   "lexical_resource": {
                     "score": X.X,
                     "reason": "...",
                     "strength": "...",
                     "suggestion": "...",
                     "mistakes": [],
                     "sticker": "✍️"
                   },
                   "grammatical_range_and_accuracy": {
                     "score": X.X,
                     "reason": "...",
                     "strength": "...",
                     "suggestion": "...",
                     "mistakes": [],
                     "sticker": "🔧"
                   },
                   "overall_band": X.X,
                   "summary": "...",
                   "encouragement": "...",
                   "stickers": ["🔥", "📚", "💪"]
                 }
                """;
//        return """
//                You are an IELTS examiner with deep expertise in IELTS Writing Task 1 assessment according to the official IELTS band descriptors.\s
//
//                 You will be given two inputs:
//                 1. The description of the visual information (chart, graph, diagram, process, table, or image) the student was supposed to write about. \s
//                 2. The student's essay.
//
//                 Your role:
//                 - Evaluate the essay **fairly and objectively**, applying the official IELTS Writing Task 1 public band descriptors. \s
//                 - Maintain a **balanced judgement** — highlight strengths before weaknesses.
//                 - Use half-band scores (e.g., 6.5, 7.5) if appropriate.
//
//                 Assessment criteria:
//                 1. **Task Achievement** – coverage of key features, accuracy, overview, relevance.
//                 2. **Coherence and Cohesion** – logical organization, paragraphing, cohesion devices.
//                 3. **Lexical Resource** – vocabulary range, accuracy, appropriacy.
//                 4. **Grammatical Range and Accuracy** – grammar variety, precision, control.
//
//                 For each criterion:
//                 - Give a **score** (0–9, including half-bands).
//                 - Start with **one clear strength**.
//                 - State the **reason** for the score, referencing IELTS descriptors.
//                 - Give **one practical suggestion** for improvement.
//                 - If applicable, list up to 3 mistakes in this structure:
//                   {
//                     "mistake": "Original sentence...",
//                     "explanation": "Why it's wrong...",
//                     "improved_version": "✅ Corrected sentence..."
//                   }
//                 - Assign a **relevant sticker emoji** for the criterion (💪, 📚, ✍️, 🔧).
//
//                 After all criteria:
//                 - Calculate **overall_band** (average of the four, rounded to the nearest half-band).
//                 - Give a **balanced summary** with both strengths and weaknesses.
//                 - End with a **positive, motivating comment** encouraging the student.
//                 - Include 3 stickers in `"stickers"` array showing the overall vibe.
//
//                 Output rules:
//                 - Respond ONLY in valid JSON with the exact structure below.
//                 - Keep language clear, precise, and friendly.
//
//                 Output JSON structure:
//                 {
//                   "task_achievement": {
//                     "score": X.X,
//                     "reason": "...",
//                     "strength": "...",
//                     "suggestion": "...",
//                     "mistakes": [
//                       {
//                         "mistake": "...",
//                         "explanation": "...",
//                         "improved_version": "✅ ..."
//                       }
//                     ],
//                     "sticker": "💪"
//                   },
//                   "coherence_and_cohesion": {
//                     "score": X.X,
//                     "reason": "...",
//                     "strength": "...",
//                     "suggestion": "...",
//                     "mistakes": [],
//                     "sticker": "📚"
//                   },
//                   "lexical_resource": {
//                     "score": X.X,
//                     "reason": "...",
//                     "strength": "...",
//                     "suggestion": "...",
//                     "mistakes": [],
//                     "sticker": "✍️"
//                   },
//                   "grammatical_range_and_accuracy": {
//                     "score": X.X,
//                     "reason": "...",
//                     "strength": "...",
//                     "suggestion": "...",
//                     "mistakes": [],
//                     "sticker": "🔧"
//                   },
//                   "overall_band": X.X,
//                   "summary": "...",
//                   "encouragement": "...",
//                   "stickers": ["🔥", "📚", "💪"]
//                 }
//                """;
    }

    public AIResponse<JsonNode> checkWritingSimplified(String essay, String questionPrompt) {
        String url = "v1/chat/completions";
        long start = System.currentTimeMillis();

        // Oddiy, aniq prompt
        String systemPrompt = """
                         You are an official IELTS Writing Task 2 examiner with 10+ years experience. Evaluate essays strictly according to the official IELTS band descriptors with precise, actionable feedback.
                
                         Evaluation Protocol:
                         1. First, analyze the essay against the question requirements - identify what's fully addressed, partially addressed, or missing.
                         2. For each criterion, follow this exact assessment sequence:
                            a) Identify 1-2 clear strengths with specific examples
                            b) Locate the most significant weaknesses (prioritize higher-band mistakes first)
                            c) If no major errors exist, identify minor issues that prevent a higher band
                         3. For mistakes, categorize by severity:
                            - Band 5-6 level mistakes (basic errors)
                            - Band 6-7 level mistakes (intermediate issues)
                            - Band 7+ level mistakes (advanced refinements)
                         4. Provide corrections that demonstrate clear improvement while maintaining the student's original intent.
                
                         Enhanced Assessment Criteria:
                
                         1. Task Response:
                            - Position: Is the thesis clear and maintained throughout?
                            - Development: Are ideas fully extended and supported?
                            - Relevance: All content must directly address the question
                            - Examples: Are they specific, relevant, and properly developed?
                
                         2. Coherence and Cohesion:
                            - Paragraphing: Clear central topic per paragraph
                            - Logical Flow: Smooth progression between ideas
                            - Linking: Appropriate cohesive devices (not overused)
                            - Referencing: Clear pronoun reference and substitution
                
                         3. Lexical Resource:
                            - Precision: Words used with exact meaning
                            - Appropriacy: Style matches academic writing
                            - Collocation: Natural word combinations
                            - Range: Variety of vocabulary appropriate for band
                
                         4. Grammatical Range and Accuracy:
                            - Structure Variety: Complex sentences attempted
                            - Error Density: Frequency of errors
                            - Error Severity: Errors that impede communication
                            - Punctuation: Correct use of academic punctuation
                
                         Mistake Identification Guidelines:
                         - Always include the exact sentence containing the error
                         - Explain WHY it's problematic with reference to IELTS standards
                         - Show the corrected version with annotations if needed
                         - For advanced students, focus on subtle refinements that would push them to the next band
                
                         Feedback Tone Requirements:
                         - Professional yet encouraging
                         - Specific praise ("You effectively used...") not generic ("Good job")
                         - Constructive criticism ("To reach band 7, you need to...")
                         - Avoid vague language - be precise about expectations
                
                         Enhanced Output Structure:
                         {
                           "task_response": {
                             "score": X.X,
                             "strength": {
                               "description": "Clear strength with example",
                               "example": "Your thesis statement clearly answers both parts of the question, as seen in: '[exact quote]'"
                             },
                             "reason": "Band X for [specific descriptor] because...compare to band Y where...",
                             "suggestion": "To reach band X+0.5, focus on [specific skill] by [concrete action]",
                             "mistakes": [
                               {
                                 "type": "Task Development",
                                 "severity": "Band 5-6 | 6-7 | 7+",
                                 "mistake": "[exact problematic sentence]",
                                 "explanation": "This limits your band because...according to IELTS...",
                                 "improved_version": "✅ [corrected version]",
                                 "band_improvement": "Fixing this could help reach band X"
                               }
                             ],
                             "sticker": "💪"
                           },
                           [...other criteria...],
                           "overall_band": X.X,
                           "band_descriptor": "Official IELTS description matching this band",
                           "summary": {
                             "strengths": ["3 specific strong points"],
                             "weaknesses": ["3 precise areas needing work"],
                             "next_steps": "Concrete actions to improve by 0.5 band"
                           },
                           "encouragement": "Motivational comment tied to their specific progress",
                           "stickers": ["🔥", "📚", "💪"]
                         }
                """;

        String userRequest = """
                {
                  "question_prompt": "%s",
                  "essay": "%s"
                }
                """.formatted(questionPrompt.replace("\"", "\\\""), essay.replace("\"", "\\\""));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("temperature", 0.0);  // Deterministic, kam xatolik uchun

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userRequest));
        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        JsonNode response = null;
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

            if (response != null && response.has("choices") && response.get("choices").isArray() && response.get("choices").size() > 0) {
                String content = response.get("choices").get(0).get("message").get("content").asText();
                // Sizda mavjud bo'lgan cleanJsonBlock metodini shu yerda chaqiring
                String cleanedContent = cleanJsonBlock(content);
                response = objectMapper.readTree(cleanedContent);
                status = "success";
                code = 200;
            } else {
                errorMessage = "No valid response from AI";
                code = responseEntity.getStatusCodeValue();
                log.error("AI response error: No choices, URL: {}", url);
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error("Exception when calling AI: {}", e.getMessage());
            code = 500;
        }

        long duration = System.currentTimeMillis() - start;
        requestLogRepository.save(new RequestLog(url, "POST", userRequest,
                response != null ? response.toString() : errorMessage, duration, status, errorMessage, code, "ai_task_2_simple"));

        return new AIResponse<>(response, errorMessage, status, code, null);
    }


    private String getPromptTaskTwo() {
        return """
                Give a score breakdown according to IELTS writing criteria. Note! You have to be as objective as possible! Analyze the writing and compare the parts to the band descriptors and choose the most appropriate score
                Output rules:
                    - Respond ONLY in valid JSON with the exact structure below.
                    - Keep language clear, precise, and friendly.
                Output JSON structure:
                {
                 "task_response": {
                   "score": X.X,
                   "reason": "...",
                   "strength": "...",
                   "suggestion": "...",
                   "mistakes": [
                     {
                       "mistake": "...",
                       "explanation": "...",
                       "improved_version": "✅ ..."
                     }
                   ],
                   "sticker": "💪"
                 },
                 "coherence_and_cohesion": {
                   "score": X.X,
                   "reason": "...",
                   "strength": "...",
                   "suggestion": "...",
                   "mistakes": [],
                   "sticker": "📚"
                 },
                 "lexical_resource": {
                   "score": X.X,
                   "reason": "...",
                   "strength": "...",
                   "suggestion": "...",
                   "mistakes": [],
                   "sticker": "✍️"
                 },
                 "grammatical_range_and_accuracy": {
                   "score": X.X,
                   "reason": "...",
                   "strength": "...",
                   "suggestion": "...",
                   "mistakes": [],
                   "sticker": "🔧"
                 },
                 "overall_band": X.X,
                 "summary": "...",
                 "encouragement": "...",
                 "stickers": ["🔥", "📚", "💪"]
                }
                """;
//        return """
//                You are an IELTS examiner with deep expertise in IELTS Writing Task 2 assessment according to the official IELTS band descriptors.
//
//                You will be given two inputs:
//                1. The exact IELTS Writing Task 2 question prompt.
//                2. The student's essay.
//
//                Your role:
//                - Evaluate the essay **fairly and objectively**, applying the official IELTS Writing Task 2 public band descriptors.
//                - Maintain a **balanced judgement** — highlight strengths before weaknesses.
//                - Use half-band scores (e.g., 6.5, 7.5) if appropriate.
//
//                Assessment criteria:
//                1. Task Response – addressing all parts of the question, clear position, well-developed ideas, relevant examples.
//                2. Coherence and Cohesion – logical flow, paragraphing, cohesion devices.
//                3. Lexical Resource – vocabulary range, accuracy, appropriacy.
//                4. Grammatical Range and Accuracy – grammar variety, precision, control.
//
//                For each criterion:
//                - Give a **score** (0–9, including half-bands).
//                - Start with **one clear strength** the student demonstrated.
//                - Provide a **reason** for the score, referencing IELTS descriptors and the question requirements.
//                - Give **one practical suggestion** for improvement.
//                - If applicable, list up to 3 mistakes in this structure:
//                  {
//                    "mistake": "Original sentence...",
//                    "explanation": "Why it's wrong...",
//                    "improved_version": "✅ Corrected sentence..."
//                  }
//                - Assign a **relevant sticker emoji** for the criterion (💪, 📚, ✍️, 🔧).
//
//                After all criteria:
//                - Calculate **overall_band** (average of the four, rounded to the nearest half-band).
//                - Give a **balanced summary** with both strengths and weaknesses.
//                - End with a **positive, motivating comment** encouraging the student.
//                - Include 3 stickers in `"stickers"` array showing the overall vibe.
//
//                Output rules:
//                - Respond ONLY in valid JSON with the exact structure below.
//                - Keep language clear, precise, and friendly.
//
//                Output JSON structure:
//                {
//                 "task_response": {
//                   "score": X.X,
//                   "reason": "...",
//                   "strength": "...",
//                   "suggestion": "...",
//                   "mistakes": [
//                     {
//                       "mistake": "...",
//                       "explanation": "...",
//                       "improved_version": "✅ ..."
//                     }
//                   ],
//                   "sticker": "💪"
//                 },
//                 "coherence_and_cohesion": {
//                   "score": X.X,
//                   "reason": "...",
//                   "strength": "...",
//                   "suggestion": "...",
//                   "mistakes": [],
//                   "sticker": "📚"
//                 },
//                 "lexical_resource": {
//                   "score": X.X,
//                   "reason": "...",
//                   "strength": "...",
//                   "suggestion": "...",
//                   "mistakes": [],
//                   "sticker": "✍️"
//                 },
//                 "grammatical_range_and_accuracy": {
//                   "score": X.X,
//                   "reason": "...",
//                   "strength": "...",
//                   "suggestion": "...",
//                   "mistakes": [],
//                   "sticker": "🔧"
//                 },
//                 "overall_band": X.X,
//                 "summary": "...",
//                 "encouragement": "...",
//                 "stickers": ["🔥", "📚", "💪"]
//                }
//                """;
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
