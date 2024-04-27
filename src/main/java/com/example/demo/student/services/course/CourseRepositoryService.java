package com.example.demo.student.services.course;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.repositories.course.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("CourseRepositoryService")
public class CourseRepositoryService {

    private final CourseRepository courseRepository;
    @Autowired
    public CourseRepositoryService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Optional<Course> getCourse(String id) {
        return courseRepository.findCourseById(id);
    }
    public void create(Course course) {
        courseRepository.save(course);
    }
    public void update(Course course) {
        courseRepository.save(course);
    }
    public void delete(Course course){
        courseRepository.delete(course);
    }
    public List<Course> getCourses() {
        return (List<Course>) courseRepository.findAll();
    }
    public void addNewCourse(Course course) {
        List<Course> existing_courses = (List<Course>) courseRepository.findAll();
        List<String> course_names = new ArrayList<String>();
        for(Course c: existing_courses){
            course_names.add(c.getName());
        }
        if(course_names.contains(course.getName())){
            throw new IllegalStateException("course's name taken");
        } else {
            course.setStudentsIds(new ArrayList<String>());
            courseRepository.save(course);
        }
    }
    public void deleteCourse(String id) {
        boolean exists = courseRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("course doesn't exist!");
        }
        courseRepository.deleteById(id);
    }

    public void addStudentToCourse(String id, String studentId) {
        Optional<Course> course = courseRepository.findCourseById(id);
        if(course.isPresent()) {
            Course c = course.get();
            if(c.getStudentsIds() == null) {
                c.setStudentsIds(new ArrayList<String>());
            }
            if(c.getStudentsIds().contains(studentId)) {
                throw new IllegalStateException("student already in course");
            }
            ArrayList<String> students = (ArrayList<String>) c.getStudentsIds();
            students.add(studentId);
            c.setStudentsIds(students);
            courseRepository.save(c);
        } else {
            throw new IllegalStateException("course doesn't exist!");
        }
    }
}
