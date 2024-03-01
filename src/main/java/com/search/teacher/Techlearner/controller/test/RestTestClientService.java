package com.search.teacher.Techlearner.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.teacher.Techlearner.controller.test.model.RandomUserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class RestTestClientService {

    private String url = "https://randomuser.me/api/";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public RandomUserResponse getAllUsers(ReqParam reqParam) {
        String requestUrl = url + "?results=" + reqParam.getCount();
        if (!reqParam.getGender().equals("all") && (reqParam.getGender().equals("female") || reqParam.getGender().equals("male")))
            requestUrl += "&gender=" + reqParam.getGender();
        if (reqParam.getSeed() != null) requestUrl += "&seed=" + reqParam.getSeed();

        if (reqParam.getFormat() != null) requestUrl += "&format=" + reqParam.getFormat();

        logger.info("Url: {}", requestUrl);
        RandomUserResponse responseObject = restClient
                .method(HttpMethod.GET)
                .uri(requestUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(RandomUserResponse.class);

        return responseObject;
    }

    private String getResponseTextFromBody(InputStream body) {
        String response = null;
        try {
            byte[] readResponse = body.readAllBytes();
            response = objectMapper.readValue(readResponse, String.class);
        } catch (IOException e) {
            logger.error("Failed read response: {}", e.getMessage());
        }
        return response;
    }

}
