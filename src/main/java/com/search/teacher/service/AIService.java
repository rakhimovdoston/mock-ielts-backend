package com.search.teacher.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String getAnswer(String question) {
        String prompt = "What is the answer to this question?";
       return "Mock answer";
    }
}
