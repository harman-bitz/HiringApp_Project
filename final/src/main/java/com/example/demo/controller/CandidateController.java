package com.example.demo.controller;

import com.example.demo.entity.Candidate;
import com.example.demo.service.CandidateService;
import com.example.demo.service.EmailService;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.MailQueueProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailQueueProducer mailQueueProducer;

    @GetMapping
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCandidate(@RequestBody Candidate candidate) {
        Candidate savedCandidate = candidateService.save(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCandidate);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countCandidates() {
        long count = candidateService.countCandidates();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Candidate candidate = candidateService.getCandidateById(id); // throws if not found
        return ResponseEntity.ok(candidate);
    }

    @PutMapping("/{id}/update-personal-info")
    public ResponseEntity<?> updatePersonalInfo(@PathVariable Long id,
                                                @RequestBody String personalInfo) {
        Candidate candidate = candidateService.getCandidateById(id); // throws if not found
        candidate.setPersonalInfo(personalInfo);
        Candidate updated = candidateService.save(candidate);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/update-bank-info")
    public ResponseEntity<?> updateBankInfo(@PathVariable Long id,
                                            @RequestBody String bankInfo) {
        Candidate candidate = candidateService.getCandidateById(id); // throws if not found
        candidate.setBankInfo(bankInfo);
        Candidate updated = candidateService.save(candidate);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/update-educational-info")
    public ResponseEntity<?> updateEducationalInfo(@PathVariable Long id,
                                                   @RequestBody String educationalInfo) {
        Candidate candidate = candidateService.getCandidateById(id); // throws if not found
        candidate.setEducationalInfo(educationalInfo);
        Candidate updated = candidateService.save(candidate);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status) {
        candidateService.updateStatus(id, status); // throws if candidate not found or invalid status
        return ResponseEntity.ok("Status updated successfully");
    }

    @PostMapping("/{id}/notify-job-offer")
    public ResponseEntity<String> sendEmail(@PathVariable Long id) {
        Candidate c = candidateService.getCandidateById(id); // throws if not found
        emailService.sendJobOfferEmail(c.getEmail(), c.getName());
        return ResponseEntity.ok("Email sent to " + c.getEmail());
    }

    @PostMapping("/{id}/notify-job-offer-async")
    public ResponseEntity<String> sendEmailViaQueue(@PathVariable Long id) {
        Candidate c = candidateService.getCandidateById(id); // throws if not found
        String jsonMessage = "{\"email\": \"" + c.getEmail() + "\", \"name\": \"" + c.getName() + "\"}";
        mailQueueProducer.sendToQueue(jsonMessage);
        return ResponseEntity.ok("Message sent to queue for " + c.getEmail());
    }

    @GetMapping("/{id}/notify-status-async")
    public ResponseEntity<String> sendStatusBasedEmailViaQueue(@PathVariable Long id) {
        Candidate candidate = candidateService.getCandidateById(id); // throws if not found
        String status = candidate.getStatus();
        String message;

        if ("selected".equalsIgnoreCase(status)) {
            message = "Congratulations " + candidate.getName() + ", you are selected!";
        } else if ("rejected".equalsIgnoreCase(status)) {
            message = "Sorry " + candidate.getName() + ", you are rejected. Our team is reviewing your status.";
        } else {
            message = "Hello " + candidate.getName() + ", our team is reviewing your status.";
        }

        String jsonMessage = String.format(
                "{\"email\": \"%s\", \"name\": \"%s\", \"message\": \"%s\"}",
                candidate.getEmail(), candidate.getName(), message
        );

        mailQueueProducer.sendToQueue(jsonMessage);
        return ResponseEntity.ok("Status message sent to queue for " + candidate.getEmail());
    }

//    @PutMapping("/{id}/update-info")
//    public ResponseEntity<?> updateInfo(@PathVariable Long id,
//                                        @RequestBody Candidate updatedInfo) {
//        Candidate updatedCandidate = candidateService.updateInfo(id, updatedInfo.getPersonalInfo(),
//                updatedInfo.getBankInfo(), updatedInfo.getEducationalInfo()); // throws if not found
//        return ResponseEntity.ok(updatedCandidate);
//    }

    @PostMapping("/{id}/upload-document")
    public ResponseEntity<?> uploadDocument(@PathVariable Long id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            Candidate candidate = candidateService.getCandidateById(id); // throws if not found

            candidate.setDocumentUploaded(true);
            candidate.setDocumentName(file.getOriginalFilename());
            candidate.setDocumentType(file.getContentType());
            candidate.setDocumentData(file.getBytes());

            candidateService.save(candidate);

            return ResponseEntity.ok("Document uploaded and saved to DB successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload document: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/verify-document")
    public ResponseEntity<?> verifyDocument(@PathVariable Long id) {
        Candidate updatedCandidate = candidateService.verifyDocument(id); // throws if not found or doc not uploaded
        return ResponseEntity.ok("Document verified successfully");
    }
}