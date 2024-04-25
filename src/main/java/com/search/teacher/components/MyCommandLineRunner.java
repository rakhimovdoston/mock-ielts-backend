package com.search.teacher.components;

import com.search.teacher.config.rabbit.RabbitMqProducer;
import com.search.teacher.model.entities.Role;
import com.search.teacher.model.enums.RoleType;
import com.search.teacher.service.RoleService;
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
    private final RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
    }
}
