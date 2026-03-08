package com.institute.scrs.repository;

import com.institute.scrs.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    List<Student> findByStdNameContainingIgnoreCase(String stdName);

    List<Student> findByCrs_CrsName(String crsName);
    boolean existsByEmail(String email);
}