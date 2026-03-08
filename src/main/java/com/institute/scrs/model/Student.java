package com.institute.scrs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    public enum StdStatus {
        ACTIVE, INACTIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Std name is required")
    private String stdName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter valid email")
    @Column(unique = true)
    private String email;

    private String phNumber;

    private LocalDate enrollDate;

    @Enumerated(EnumType.STRING)
    private StdStatus status = StdStatus.ACTIVE;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course crs;

    public Student() {}

    public Student(String stdName, String email,
                   String phNumber, LocalDate enrollDate,
                   StdStatus status, Course crs) {
        this.stdName = stdName;
        this.email = email;
        this.phNumber = phNumber;
        this.enrollDate = enrollDate;
        this.status = status;
        this.crs = crs;
    }

    public Long getId() { return id; }

    public String getStdName() { return stdName; }
    public void setStdName(String stdName) { this.stdName = stdName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhNumber() { return phNumber; }
    public void setPhNumber(String phNumber) { this.phNumber = phNumber; }

    public LocalDate getEnrollDate() { return enrollDate; }
    public void setEnrollDate(LocalDate enrollDate) { this.enrollDate = enrollDate; }

    public StdStatus getStatus() { return status; }
    public void setStatus(StdStatus status) { this.status = status; }

    public Course getCrs() { return crs; }
    public void setCrs(Course crs) { this.crs = crs; }
}