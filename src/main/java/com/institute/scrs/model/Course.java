package com.institute.scrs.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Course name is required")
    private String crsName;

    @Min(value = 1, message = "Duration must be atleast 1 week")
    private int duration;

    private String instructorName;

    private double crsFee;

    @Min(value = 0, message = "Seats cant be negative")
    private int availableSeats;

    @OneToMany(mappedBy = "crs", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Student> students;

    public Course() {}

    public Course(String crsName, int duration,
                  String instructorName, double crsFee,
                  int availableSeats) {
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
    public void setDuration(int duration) { this.duration = duration; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public double getCrsFee() { return crsFee; }
    public void setCrsFee(double crsFee) { this.crsFee = crsFee; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }
}