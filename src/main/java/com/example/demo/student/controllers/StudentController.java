package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Student;
import com.example.demo.student.services.StudentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student")

public class StudentController {

    private final StudentRepositoryService studentRepositoryService;

    @Autowired
    public StudentController(StudentRepositoryService studentRepositoryService) {
        this.studentRepositoryService = studentRepositoryService;
        Course newCourse = new Course(null,"Course1", "AN1", "12:00", "PR");
        this.studentRepositoryService.getAllCourses().add(newCourse);
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
        student.setCourseList(studentRepositoryService.allCourses);
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
        List<Student> studentList = studentRepositoryService.getStudents();
        studentRepositoryService.getAllCourses().add(course);
        for(int i=0; i<studentList.size(); i++){
            studentList.get(i).getCourseList().add(course);
            studentRepositoryService.update(studentList.get(i));
        }
    }

    @GetMapping(path = "/allCourses")
    public List<Course> getAllCourses(){
        return studentRepositoryService.getAllCourses();
    }

}
