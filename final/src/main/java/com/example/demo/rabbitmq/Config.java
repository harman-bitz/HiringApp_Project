package com.example.demo.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    public static final String QUEUE = "mail_queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, false);
    }
}