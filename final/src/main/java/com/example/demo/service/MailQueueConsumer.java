package com.example.demo.service;

import com.example.demo.rabbitmq.Config;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MailQueueConsumer {

    private final EmailService mailService;

    public MailQueueConsumer(EmailService mailService) {
        this.mailService = mailService;
    }

    @RabbitListener(queues = Config.QUEUE)
    public void receiveMessage(String message) {
        mailService.sendEmailFromQueue(message);
    }
}
