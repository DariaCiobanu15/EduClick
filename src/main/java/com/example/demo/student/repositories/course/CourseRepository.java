package com.example.demo.student.repositories.course;
import com.example.demo.student.componentObj.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CourseRepository extends CrudRepository<Course, String> {
    Optional<Course> findCourseById(String id);
}
