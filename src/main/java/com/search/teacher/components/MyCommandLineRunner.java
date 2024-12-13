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


    @Override
    public void run(String... args) throws Exception {
    }
}
