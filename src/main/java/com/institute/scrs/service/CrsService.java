package com.institute.scrs.service;

import com.institute.scrs.model.Course;
import com.institute.scrs.repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CrsService {

    @Autowired
    private CourseRepo crsRepo;

    public List<Course> getAllCourses() {
        return crsRepo.findAll();
    }

    public Course getCrsById(Long id) {
        return crsRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
    }

    public Course addCourse(Course crs) {
        return crsRepo.save(crs);
    }

    public Course updateCourse(Long id, Course updatedCrs) {
        Course existing = getCrsById(id);
        existing.setCrsName(updatedCrs.getCrsName());
        existing.setDuration(updatedCrs.getDuration());
        existing.setInstructorName(updatedCrs.getInstructorName());
        existing.setCrsFee(updatedCrs.getCrsFee());
        existing.setAvailableSeats(updatedCrs.getAvailableSeats());
        return crsRepo.save(existing);
    }

    public void deleteCourse(Long id) {
        getCrsById(id);
        crsRepo.deleteById(id);
    }
}