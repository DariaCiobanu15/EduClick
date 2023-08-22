package com.example.demo.student.services;
import com.example.demo.student.componentObj.Teacher;
import java.util.List;
import java.util.Optional;

public interface TeacherService {
    void create(Teacher teacher);
    void update(Teacher teacher);
    void delete(Teacher teacher);
    Optional<Teacher> findOne(String id);
    List<Teacher> findAll();
    Teacher findByUsername(String username);
}
