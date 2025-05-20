//package com.example.demo.entity;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Table(name = "candidate")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Candidate {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String email;
//    private String password;
//    
//    private String status;
//
//    private String personalInfo;
//    private String bankInfo;
//    private String educationalInfo;
//
//    private Boolean documentUploaded;
//    private Boolean documentVerified;
//    
//    private String otp;
//    private LocalDateTime otpExpiry;
//}

package com.example.demo.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "candidate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;
    
    private String status;

    @Column(name = "personal_info")
    private String personalInfo;

    @Column(name = "bank_info")
    private String bankInfo;

    @Column(name = "educational_info")
    private String educationalInfo;

    @Column(name = "document_uploaded")
    private Boolean documentUploaded;

    @Column(name = "document_verified")
    private Boolean documentVerified;

    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    // These fields are needed to store file in DB
    @Lob
    @Column(name = "document_data", columnDefinition = "LONGBLOB")
    private byte[] documentData;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "document_type")
    private String documentType;
}
