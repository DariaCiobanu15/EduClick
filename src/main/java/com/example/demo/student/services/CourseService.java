package com.example.demo.student.services;
import com.example.demo.student.componentObj.Course;

import java.util.List;
import java.util.Optional;


public interface CourseService {
    void create(Course course);
    void update(Course course);
    void delete(Course course);

    Optional<Course> findOne(String id);

    List<Course> findAll();

    List<Course> findByName(String name);
}
