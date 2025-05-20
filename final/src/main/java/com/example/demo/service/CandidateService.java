//package com.example.demo.service;
//
//
//import com.example.demo.entity.Candidate;
//import com.example.demo.repository.CandidateRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CandidateService {
//
//    @Autowired
//    private CandidateRepository repo;
//    
//    @Autowired
//    private CandidateRepository candidateRepository;
//
//    public List<Candidate> getAllCandidates() {
//        return repo.findAll();
//    }
//
//    public Candidate getCandidateById(Long id) {
//        return repo.findById(id).orElse(null);
//    }
//
//    public Candidate updateStatus(Long id, String status) {
//        Candidate candidate = getCandidateById(id);
//        if (candidate != null) {
//            candidate.setStatus(status);
//            return repo.save(candidate);
//        }
//        return null;
//    }
//
//    public Candidate updateInfo(Long id, String personal, String bank, String edu) {
//        Candidate candidate = getCandidateById(id);
//        if (candidate != null) {
//            candidate.setPersonalInfo(personal);
//            candidate.setBankInfo(bank);
//            candidate.setEducationalInfo(edu);
//            return repo.save(candidate);
//        }
//        return null;
//    }
//
//    public Candidate uploadDocument(Long id) {
//        Candidate candidate = getCandidateById(id);
//        if (candidate != null) {
//            candidate.setDocumentUploaded(true);
//            return repo.save(candidate);
//        }
//        return null;
//    }
//    
//   
//    public Candidate verifyDocument(Long id) {
//        Candidate candidate = getCandidateById(id);
//        if (candidate != null && Boolean.TRUE.equals(candidate.getDocumentUploaded())) {
//            candidate.setDocumentVerified(true);
//            return repo.save(candidate);
//        }
//        return null;
//    }
//    
//    public Candidate save(Candidate candidate) {
//        return candidateRepository.save(candidate);
//    }
//
//}
//
//


package com.example.demo.service;

import com.example.demo.entity.Candidate;
import com.example.demo.exception.CandidateNotFoundException;
import com.example.demo.exception.InvalidStatusException;
import com.example.demo.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository repo;

    @Autowired
    private CandidateRepository candidateRepository;

    public List<Candidate> getAllCandidates() {
        return repo.findAll();
    }

    public Candidate getCandidateById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));
    }

    public Candidate updateStatus(Long id, String status) {
        if (!isValidStatus(status)) {
            throw new InvalidStatusException(status);
        }
        Candidate candidate = getCandidateById(id);
        candidate.setStatus(status);
        return repo.save(candidate);
    }

    public long countCandidates() {
        return candidateRepository.count();
    }

    public Candidate updateInfo(Long id, String personal, String bank, String edu) {
        Candidate candidate = getCandidateById(id);
        candidate.setPersonalInfo(personal);
        candidate.setBankInfo(bank);
        candidate.setEducationalInfo(edu);
        return repo.save(candidate);
    }

    public Candidate uploadDocument(Long id) {
        Candidate candidate = getCandidateById(id);
        candidate.setDocumentUploaded(true);
        return repo.save(candidate);
    }

    public Candidate verifyDocument(Long id) {
        Candidate candidate = getCandidateById(id);
        if (Boolean.TRUE.equals(candidate.getDocumentUploaded())) {
            candidate.setDocumentVerified(true);
            return repo.save(candidate);
        } else {
            throw new InvalidStatusException("Document not uploaded yet for candidate ID " + id);
        }
    }

    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    // Helper method for status validation
    private boolean isValidStatus(String status) {
        return status != null && (status.equalsIgnoreCase("Pending")
                || status.equalsIgnoreCase("Rejected")
                || status.equalsIgnoreCase("Selected"));
    }
}
