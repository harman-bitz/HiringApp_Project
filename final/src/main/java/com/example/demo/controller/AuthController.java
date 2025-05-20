package com.example.demo.controller;

import com.example.demo.entity.Candidate;
import com.example.demo.service.AuthService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    
    @PostMapping("/signup")
    public String signup(@RequestBody Candidate candidate) {
        authService.signUp(candidate);
        return "Signup successful!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request.getEmail(), request.getPassword()); // returns JWT
    }


    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        authService.sendOtp(email);
        return "OTP sent to email.";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return authService.verifyOtp(email, otp) ? "OTP Verified." : "Invalid OTP.";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        authService.resetPassword(email, newPassword);
        return "Password updated successfully.";
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
