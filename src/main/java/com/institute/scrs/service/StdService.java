package com.institute.scrs.service;

import com.institute.scrs.model.Student;
import com.institute.scrs.model.Course;
import com.institute.scrs.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class StdService {

    @Autowired
    private StudentRepo stdRepo;

    @Autowired
    private CrsService crsService;

    public List<Student> getAllStudents() {
        return stdRepo.findAll();
    }

    public Student getStdById(Long id) {
        return stdRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    public Student addStudent(Student std) {

         if (stdRepo.existsByEmail(std.getEmail())) {
        throw new RuntimeException("Email already registered!");
    }

   
    Course crs = crsService.getCrsById(std.getCrs().getId());
    if (crs.getAvailableSeats() <= 0) {
        throw new RuntimeException("No seats available in: " + crs.getCrsName());
    }

       
        crs.setAvailableSeats(crs.getAvailableSeats() - 1);
        crsService.addCourse(crs);

       
        std.setEnrollDate(LocalDate.now());
        std.setCrs(crs);

        return stdRepo.save(std);
    }

    public Student updateStudent(Long id, Student updatedStd) {
        Student existing = getStdById(id);
        existing.setStdName(updatedStd.getStdName());
        existing.setEmail(updatedStd.getEmail());
        existing.setPhNumber(updatedStd.getPhNumber());
        existing.setStatus(updatedStd.getStatus());
        return stdRepo.save(existing);
    }

    public void deleteStudent(Long id) {
        getStdById(id);
        stdRepo.deleteById(id);
    }

    public List<Student> searchByName(String name) {
        return stdRepo.findByStdNameContainingIgnoreCase(name);
    }

    public List<Student> filterByCourse(String crsName) {
        return stdRepo.findByCrs_CrsName(crsName);
    }
}