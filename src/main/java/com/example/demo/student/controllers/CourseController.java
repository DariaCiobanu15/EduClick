package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Post;
import com.example.demo.student.services.CourseRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewCourse(@RequestBody Course course){
        courseRepositoryService.addNewCourse(course);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "{courseId}/delete")
    public void deleteCourse(@PathVariable("courseId") String courseId){
        courseRepositoryService.deleteCourse(courseId);
    }
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    @PutMapping(path = "/update/{courseId}")
    public void updateCourse(@PathVariable("courseId") String courseId, @Valid @RequestBody Course course){
        course.setId(courseId);
        courseRepositoryService.update(course);
    }

    @PreAuthorize("hasRole('ROLE_teacher')")
    @PutMapping(path = "/updateDescription/{courseId}")
    public void updateDescription(@PathVariable("courseId") String courseId, @Valid @RequestBody String description){
        Optional<Course> optionalCourse = courseRepositoryService.getCourse(courseId);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setDescription(description);
            courseRepositoryService.update(course);
        }
    }
    @PreAuthorize("hasRole('ROLE_teacher')")
    @PutMapping(path = "/updatePosts/{courseId}")
    public void updatePosts(@PathVariable("courseId") String courseId, @Valid @RequestBody Post post){
        Optional<Course> optionalCourse = courseRepositoryService.getCourse(courseId);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            String monthName = dateFormat.format(date);
            String day = dayFormat.format(date);
            String year = yearFormat.format(date);
            post.setMonth(monthName);
            post.setDay(day);
            post.setYear(year);

            if(course.getPosts() == null){
                List<Post> pposts = new ArrayList<>();
                pposts.add(post);
                course.setPosts(pposts);
            }
            course.getPosts().add(post);
            courseRepositoryService.update(course);
        }
    }

    @GetMapping(path = "/getPosts/{courseId}")
    public List<Post> getPosts(@PathVariable("courseId") String courseId) {
        Optional<Course> optionalCourse = courseRepositoryService.getCourse(courseId);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<Post> posts = course.getPosts();
            return posts;
        }
        else return null;
    }
}
