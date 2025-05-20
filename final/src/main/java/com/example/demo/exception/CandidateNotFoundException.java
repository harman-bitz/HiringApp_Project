package com.example.demo.exception;

public class CandidateNotFoundException extends RuntimeException {
    public CandidateNotFoundException(Long id) {
        super("Candidate with ID " + id + " does not exist");
    }
}