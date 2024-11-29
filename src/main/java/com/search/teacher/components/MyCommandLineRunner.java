package com.search.teacher.components;

import com.search.teacher.config.rabbit.RabbitMqProducer;
import com.search.teacher.service.JsoupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RabbitMqProducer rabbitMqProducer;
    private final JsoupService jsoupService;
    String content = "<p>ajkskd asdjc aj cd asd <input class=\"reading-question-inputs\" type=\"text\" placeholder=\"10\" value=\"\" id=\"ques-10\" tabindex=\"10\" style=\"display: inline-block; width: 150px; text-align: center; border: 1px solid gray; border-radius: 20px; padding: 4px 8px; outline: none; vertical-align: middle;\"> ajsdjcjasd ajds asd<input class=\"reading-question-inputs\" type=\"text\" placeholder=\"11\" value=\"\" id=\"ques-11\" tabindex=\"11\" style=\"display: inline-block; width: 150px; text-align: center; border: 1px solid gray; border-radius: 20px; padding: 4px 8px; outline: none; vertical-align: middle;\"> ajsdjkcjd qkc diuqc jidwqc <input class=\"reading-question-inputs\" type=\"text\" placeholder=\"12\" value=\"\" id=\"ques-12\" tabindex=\"12\" style=\"display: inline-block; width: 150px; text-align: center; border: 1px solid gray; border-radius: 20px; padding: 4px 8px; outline: none; vertical-align: middle;\"> jqwocadnqiwoc oqwe cqwo <input class=\"reading-question-inputs\" type=\"text\" placeholder=\"13\" value=\"\" id=\"ques-13\" tabindex=\"13\" style=\"display: inline-block; width: 150px; text-align: center; border: 1px solid gray; border-radius: 20px; padding: 4px 8px; outline: none; vertical-align: middle;\"> jqwedieowqnciqw coqwec </p>";

    @Override
    public void run(String... args) throws Exception {
    }
}
