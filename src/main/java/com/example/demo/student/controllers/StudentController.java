package com.example.demo.student.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Student;
import com.example.demo.student.services.StudentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student")

public class StudentController {

    private final StudentRepositoryService studentRepositoryService;

    @Autowired
    public StudentController(StudentRepositoryService studentRepositoryService) {
        this.studentRepositoryService = studentRepositoryService;
        String id = UUID.randomUUID().toString();
        Course newCourse = new Course( id ,"Operating Systems", "AN1", "12:00", "PR");

        List<Course> courseList = this.studentRepositoryService.getAllCourses();
        if (courseList == null) {
            courseList = new ArrayList<>(); // Initialize a new list if it's null
            this.studentRepositoryService.setAllCourses(courseList); // Update the courses in the service
        }

        courseList.add(newCourse); // Add the new course to the list
    }

    @GetMapping(path = "/all")
    public List<Student> getStudents(){
        return studentRepositoryService.getStudents();
    }
    @GetMapping(path = "{studentId}")
    public Optional<Student> getStudent(@PathVariable("studentId") String studentId){
        return studentRepositoryService.getStudent(studentId);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewStudent(@RequestBody Student student){
        student.setCourseList(studentRepositoryService.getAllCourses());
        studentRepositoryService.addNewStudent(student);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "{studentId}/delete")
    public void deleteStudent(@PathVariable("studentId") String studentId){
        studentRepositoryService.deleteStudent(studentId);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/update/{studentId}")
    public void updateStudent(@PathVariable("studentId") String studentId, @Valid @RequestBody Student student){
        student.setId(studentId);
        studentRepositoryService.update(student);
    }
    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/registerCourse")
    public void registerCourse(@Valid @RequestBody Course course){
        System.out.println(course);
        List<Student> studentList = studentRepositoryService.getStudents();
        List<Course> courseList = studentRepositoryService.getAllCourses();
        courseList.add(course);
        for(int i=0; i<studentList.size(); i++){
            studentList.get(i).setCourseList(courseList);
            studentRepositoryService.update(studentList.get(i));
        }
        this.studentRepositoryService.setAllCourses(courseList);
    }

    @GetMapping(path = "/allCourses")
    public List<Course> getAllCourses(){
        List<Student> students = (List<Student>) studentRepositoryService.findAll();
        return students.get(0).getCourseList();
    }

}
