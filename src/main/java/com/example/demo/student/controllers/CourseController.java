package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.services.CourseRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/course")
public class CourseController {
    private final CourseRepositoryService courseRepositoryService;

    @Autowired
    public CourseController(CourseRepositoryService courseRepositoryService){
        this.courseRepositoryService = courseRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Course> getCourses(){
        return courseRepositoryService.getCourses();
    }

    @GetMapping(path = "{courseId}")
    public Optional<Course> getCourse(@PathVariable("courseId") String courseId){
        return courseRepositoryService.getCourse(courseId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewCourse(@RequestBody Course course){
        courseRepositoryService.addNewCourse(course);
    }

    @DeleteMapping(path = "{courseId}/delete")
    public void deleteCourse(@PathVariable("courseId") String courseId){
        courseRepositoryService.deleteCourse(courseId);
    }
    @PutMapping(path = "/update/{courseId}")
    public void updateCourse(@PathVariable("courseId") String courseId, @Valid @RequestBody Course course){
        course.setId(courseId);
        courseRepositoryService.update(course);
    }

    @PutMapping(path = "/updateDescription/{courseId}")
    public void updateDescription(@PathVariable("courseId") String courseId, @Valid @RequestBody String description){
        Optional<Course> optionalCourse = courseRepositoryService.getCourse(courseId);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setDescription(description);
            courseRepositoryService.update(course);
        }
    }
    @PutMapping(path = "/updatePosts/{courseId}")
    public void updatePosts(@PathVariable("courseId") String courseId, @Valid @RequestBody String post){
        Optional<Course> optionalCourse = courseRepositoryService.getCourse(courseId);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.getPosts().add(post); //poate trebuie setPosts
            courseRepositoryService.update(course);
        }
    }
}
