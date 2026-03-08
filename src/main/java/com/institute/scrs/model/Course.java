package com.institute.scrs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    private String crsName;

    @Min(value = 1, message = "Duration must be atleast 1 week")
    private Integer duration;

    private String instructorName;

    private Double crsFee;

    @Min(value = 0, message = "Seats cant be negative")
    private Integer availableSeats;

    @OneToMany(mappedBy = "crs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students;

    public Course() {}

    public Course(String crsName, int duration,
                  String instructorName, Double crsFee,
                  Integer  availableSeats) {
        this.crsName = crsName;
        this.duration = duration;
        this.instructorName = instructorName;
        this.crsFee = crsFee;
        this.availableSeats = availableSeats;
    }

    public Long getId() { return id; }

    public String getCrsName() { return crsName; }
    public void setCrsName(String crsName) { this.crsName = crsName; }

    public int getDuration() { return duration; }
    public void setDuration(Integer  duration) { this.duration = duration; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public double getCrsFee() { return crsFee != null ? crsFee : 0.0; }
    public void setCrsFee(Double crsFee) { this.crsFee = crsFee; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}