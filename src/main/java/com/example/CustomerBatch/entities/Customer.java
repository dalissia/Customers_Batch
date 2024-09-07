package com.example.CustomerBatch.entities;

import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String phoneNumber;
    private String country;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dob;
}
