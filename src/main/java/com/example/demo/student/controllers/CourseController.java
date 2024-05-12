package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Post;
import com.example.demo.student.componentObj.Student;
import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.post.PostRepositoryService;
import com.example.demo.student.services.student.StudentRepositoryService;
import com.example.demo.student.services.teacher.TeacherRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/course")
public class CourseController {
    private final CourseRepositoryService courseRepositoryService;
    private final StudentRepositoryService studentRepositoryService;
    private final TeacherRepositoryService teacherRepositoryService;

    private final PostRepositoryService postRepositoryService;

    @Autowired
    public CourseController(CourseRepositoryService courseRepositoryService,
                            StudentRepositoryService studentRepositoryService,
                            TeacherRepositoryService teacherRepositoryService,
                            PostRepositoryService postRepositoryService) {
        this.courseRepositoryService = courseRepositoryService;
        this.studentRepositoryService = studentRepositoryService;
        this.teacherRepositoryService = teacherRepositoryService;
        this.postRepositoryService = postRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Course> getCourses(){
        return courseRepositoryService.getCourses();
    }

    @GetMapping(path = "/{courseId}")
    public Optional<Course> getCourse(@PathVariable("courseId") String courseId){
        return courseRepositoryService.getCourse(courseId);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public Course registerNewCourse(@RequestBody Course course){
        courseRepositoryService.addNewCourse(course);
        return course;
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

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/assignTeacher/{courseId}/{teacherId}")
    public void assignTeacherToCourse(@PathVariable String courseId, @PathVariable String teacherId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        Optional<Teacher> teacherOptional = teacherRepositoryService.getTeacher(teacherId);

        if (courseOptional.isPresent() && teacherOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setTeacherId(teacherId);
            courseRepositoryService.update(course);
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/assignLabTeacher/{courseId}/{teacherId}")
    public void assignLabTeacherToCourse(@PathVariable String courseId, @PathVariable String teacherId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        Optional<Teacher> teacherOptional = teacherRepositoryService.getTeacher(teacherId);

        if (courseOptional.isPresent() && teacherOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setLabTeacherId(teacherId);
            courseRepositoryService.update(course);
        }
    }

    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    @PutMapping(path = "/enroll/{studentId}")
    @Transactional
    public void enrollStudentToCourse(@Valid @RequestBody Course course, @PathVariable String studentId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(course.getId());
        System.out.println(studentId);
        if (courseOptional.isPresent()) {
            Course c = courseOptional.get();
            System.out.println(c);
            ArrayList<String> students = (ArrayList<String>) c.getStudentsIds();
            if (students.contains(studentId)) {
                throw new IllegalStateException("student already in course");
            }
            students.add(studentId);
            c.setStudentsIds(students);
            System.out.println(c.getStudentsIds());
            courseRepositoryService.update(c);
        }
    }


    @GetMapping(path = "/{courseId}/posts")
    public ResponseEntity<List<Post>> getPostsByCourse(@PathVariable String courseId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        List<String> postIds = course.getPostsIds();
        List<Post> posts = new ArrayList<>();

        for (String postId : postIds) {
            Optional<Post> post = postRepositoryService.getPost(postId);
            post.ifPresent(posts::add);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}
