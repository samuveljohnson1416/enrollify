package com.institute.scrs.controller;
import com.institute.scrs.model.Course;
import com.institute.scrs.service.CrsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CrsController {

    @Autowired
    private CrsService crsService;

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        return ResponseEntity.ok(crsService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        return ResponseEntity.ok(crsService.getCrsById(id));
    }

    @PostMapping
    public ResponseEntity<Course> addCourse(@Valid @RequestBody Course crs) {
        return ResponseEntity.status(HttpStatus.CREATED).body(crsService.addCourse(crs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id,
                                               @Valid @RequestBody Course crs) {
        return ResponseEntity.ok(crsService.updateCourse(id, crs));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id) {
        crsService.deleteCourse(id);
        return ResponseEntity.ok("Course deleted successfully");
    }
    @GetMapping("/stats")
public ResponseEntity<Map<String, Long>> getStats() {
    Map<String, Long> stats = new HashMap<>();
    stats.put("totalCourses", crsService.getAllCourses().size() + 0L);
    stats.put("totalSeats", crsService.getAllCourses()
            .stream().mapToLong(c -> c.getAvailableSeats()).sum());
    return ResponseEntity.ok(stats);
}
}