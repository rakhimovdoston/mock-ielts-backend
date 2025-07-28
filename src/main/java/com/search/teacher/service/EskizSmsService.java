package com.search.teacher.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.model.entities.RequestLog;
import com.search.teacher.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EskizSmsService {

    @Value("${sms.url}")
    private String smsUrl;

    @Value("${sms.api-key}")
    private String apiKey;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final RequestLogRepository requestLogRepository;
    private final RestTemplate restSmsTemplate;
    private final ObjectMapper objectMapper;

    public EskizSmsService(RequestLogRepository requestLogRepository, @Qualifier("smsTemplate") RestTemplate restSmsTemplate, ObjectMapper objectMapper) {
        this.requestLogRepository = requestLogRepository;
        this.restSmsTemplate = restSmsTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendSms(String phoneNumber, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("phone_number", phoneNumber);
        body.put("message", message);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        long start = System.currentTimeMillis();
        JsonNode responseBody = null;
        String responseStatus = "error";
        String errorMessage = "";
        int statusCode = 0;

        try {
            ResponseEntity<JsonNode> response = restSmsTemplate.postForEntity(smsUrl, requestEntity, JsonNode.class);
            responseBody = response.getBody();
            statusCode = response.getStatusCodeValue();

            if (response.getStatusCode().is2xxSuccessful()) {
                responseStatus = "success";
            } else {
                errorMessage = "Non-success HTTP status: " + response.getStatusCode();
            }

        } catch (HttpStatusCodeException e) {
            statusCode = e.getStatusCode().value();
            String errorBody = e.getResponseBodyAsString();

            try {
                responseBody = objectMapper.readTree(errorBody);
                errorMessage = responseBody.has("message") ? responseBody.get("message").asText() : "Unknown API error";
            } catch (Exception parseException) {
                log.error("Error parsing error response: {}", parseException.getMessage());
                errorMessage = "Invalid JSON error: " + errorBody;
            }
            log.error("Eskiz API returned error: {}", errorMessage);
        } catch (Exception e) {
            statusCode = 500;
            errorMessage = "Unexpected error: " + e.getMessage();
            log.error("Unexpected error while sending SMS: {}", e.getMessage(), e);
        } finally {
            long duration = System.currentTimeMillis() - start;

            RequestLog logEntry = new RequestLog();
            logEntry.setUrl(smsUrl);
            logEntry.setMethod("POST");
            logEntry.setRequestBody(body.toString());
            logEntry.setResponseBody(responseBody != null ? responseBody.toString() : null);
            logEntry.setDuration(duration);
            logEntry.setStatus(String.valueOf(statusCode));
            logEntry.setError(errorMessage);
            logEntry.setCode(statusCode);
            requestLogRepository.save(logEntry);
        }

        return responseStatus;
    }
}
