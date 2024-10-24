//package com.search.teacher.config.rabbit;
//
//import org.springframework.amqp.rabbit.annotation.EnableRabbit;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.DefaultClassMapper;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableRabbit
//public class RabbitMQConfig {
//
//    @Bean
//    public Jackson2JsonMessageConverter converter() {
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        converter.setClassMapper(classMapper());
//        return converter;
//    }
//
//    @Bean("rabbitListenerContainerFactory")
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(converter());
//        factory.setConcurrentConsumers(2);
//        factory.setMaxConcurrentConsumers(15);
//        factory.setPrefetchCount(100);
//        return factory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(converter());
//        return template;
//    }
//
//    @Bean
//    public DefaultClassMapper classMapper() {
//        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
//        Map<String, Class<?>> map = new HashMap<>();
//        map.put(TypeIdConstants.EMAIL_PAYLOAD, EmailPayload.class);
//        defaultClassMapper.setIdClassMapping(map);
//        return defaultClassMapper;
//    }
//}
