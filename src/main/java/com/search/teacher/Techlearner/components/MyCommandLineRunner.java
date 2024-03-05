package com.search.teacher.Techlearner.components;

import com.search.teacher.Techlearner.config.rabbit.RabbitMqProducer;
import com.search.teacher.Techlearner.model.entities.Role;
import com.search.teacher.Techlearner.model.enums.RoleType;
import com.search.teacher.Techlearner.service.RoleService;
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
