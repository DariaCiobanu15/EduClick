package com.example.demo.student.repositories.teacher;
import com.example.demo.student.componentObj.Teacher;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, String> {
    Optional<Teacher> findById(String studentId);
}
