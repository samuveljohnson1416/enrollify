package com.institute.scrs.controller;

import com.institute.scrs.model.Student;
import com.institute.scrs.service.StdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StdController {

    @Autowired
    private StdService stdService;

    @GetMapping
    public ResponseEntity<List<Student>> getAll() {
        return ResponseEntity.ok(stdService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stdService.getStdById(id));
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody Student std) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stdService.addStudent(std));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id,
                                                 @Valid @RequestBody Student std) {
        return ResponseEntity.ok(stdService.updateStudent(id, std));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        stdService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Student>> search(@RequestParam String name) {
        return ResponseEntity.ok(stdService.searchByName(name));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filter(@RequestParam String crsName) {
        return ResponseEntity.ok(stdService.filterByCourse(crsName));
    }
    @GetMapping("/stats")
public ResponseEntity<Map<String, Long>> getStdStats() {
    Map<String, Long> stats = new HashMap<>();
    List<Student> all = stdService.getAllStudents();
    stats.put("totalStudents", (long) all.size());
    stats.put("activeStudents", all.stream()
            .filter(s -> s.getStatus() == Student.StdStatus.ACTIVE)
            .count());
    return ResponseEntity.ok(stats);
}
}