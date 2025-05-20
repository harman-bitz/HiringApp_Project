package com.example.demo.service;


	import com.example.demo.entity.Candidate;
	
	import com.example.demo.repository.CandidateRepository;
	
	import com.example.demo.security.JwtUtil;
	
	import org.springframework.beans.factory.annotation.Autowired;
	
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
	
	
	import org.springframework.stereotype.Service;

	import java.time.LocalDateTime;
	import java.util.Optional;
	import java.util.Random;

	@Service
	public class AuthService {

	    @Autowired
	    private CandidateRepository repo;

	    @Autowired
	    private EmailService emailService;

	    @Autowired
	    private JwtUtil jwtUtil;

	    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


	    public void signUp(Candidate candidate) {
	        candidate.setPassword(passwordEncoder.encode(candidate.getPassword()));
	        repo.save(candidate);
	    }

	    
	    
	    public String login(String email, String rawPassword) {
	        Candidate user = repo.findByEmail(email).orElseThrow();
	        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
	            return jwtUtil.generateToken(email); // 🔐 Return JWT token
	        }
	        throw new RuntimeException("Invalid credentials");
	    }


	    public void sendOtp(String email) {
	        Candidate user = repo.findByEmail(email).orElseThrow();
	        String otp = String.format("%06d", new Random().nextInt(999999));
	        user.setOtp(otp);
	        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
	        repo.save(user);
	        emailService.sendOtpEmail(email, otp);
	    }

	    public boolean verifyOtp(String email, String otp) {
	        Candidate user = repo.findByEmail(email).orElseThrow();
	        return user.getOtp().equals(otp) && LocalDateTime.now().isBefore(user.getOtpExpiry());
	    }

	    public void resetPassword(String email, String newPassword) {
	        Candidate user = repo.findByEmail(email).orElseThrow();
	        user.setPassword(passwordEncoder.encode(newPassword));
	        user.setOtp(null);
	        user.setOtpExpiry(null);
	        repo.save(user);
	    }
	}


