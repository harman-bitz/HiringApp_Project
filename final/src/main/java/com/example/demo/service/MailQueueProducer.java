package com.example.demo.service;

import com.example.demo.rabbitmq.Config;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MailQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public MailQueueProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToQueue(String message) {
        rabbitTemplate.convertAndSend(Config.QUEUE, message);
    }
}
