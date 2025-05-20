package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // For direct email sending (not via queue)
    public void sendJobOfferEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Job Offer Notification");
        message.setText("Dear " + name + ",\n\nYou have received a job offer.\n\nRegards,\nHR Team");
        mailSender.send(message);
    }

    // Triggered from RabbitMQ queue
    public void sendEmailFromQueue(String data) {
        try {
            // Parse JSON message from queue
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(data);

            String to = node.get("email").asText();
            String name = node.get("name").asText();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Job Offer Notification (Async)");
            message.setText("Dear " + name + ",\n\nYou have received a job offer.\n\nRegards,\nHR Team");

            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email from queue: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // NEW METHOD: Send OTP email for password reset
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP for Password Reset");
        message.setText("Your OTP is: " + otp + "\nThis OTP will expire in 10 minutes.");
        mailSender.send(message);
    }
}
