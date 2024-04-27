package com.example.demo.student.services.student;
import com.example.demo.student.componentObj.Student;
import java.util.List;
import java.util.Optional;


public interface StudentService {
    Optional<Student> findOne(String id);
    List<Student> findAll();
    List<Student> findByName(String firstName);
    Student findByUsername(String username);
    void create(Student student);
    void update(Student student);
    void delete(Student student);
}
