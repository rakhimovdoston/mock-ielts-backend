package com.search.teacher.Techlearner.service.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.Techlearner.model.response.JResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HttpClientService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestClient restClient;
    private final ObjectMapper objectMapper;


    public JResponse questionFromExternal() {
        JsonNode response = null;
        try {
            response = restClient.get()
                    .uri("https://opentdb.com/api.php?amount=50&type=multiple")
                    .retrieve()
                    .body(JsonNode.class);
            return JResponse.success(response);
        } catch (Exception e) {
            logger.error("Error call api from External API: {}", e.getMessage());
            return JResponse.error(503, e.getMessage());
        }
    }
}
