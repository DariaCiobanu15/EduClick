package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Student;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.student.StudentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student")

public class StudentController {

    private final StudentRepositoryService studentRepositoryService;
    private final CourseRepositoryService courseRepositoryService;

    @Autowired
    public StudentController(StudentRepositoryService studentRepositoryService, CourseRepositoryService courseRepositoryService) {
        this.studentRepositoryService = studentRepositoryService;
        this.courseRepositoryService = courseRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Student> getStudents(){
        return studentRepositoryService.getStudents();
    }
    @GetMapping(path = "{studentId}")
    public Optional<Student> getStudent(@PathVariable("studentId") String studentId){
        return studentRepositoryService.getStudent(studentId);
    }

    @GetMapping(path = "{studentId}/myCourses")
    public List<String> getCoursesFromStudent(@PathVariable("studentId") String studentId){
        Optional<Student> optionalStudent = studentRepositoryService.getStudent(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            List<String> ids = student.getCourseIds();
            return ids;
        } else {
            return null;
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewStudent(@RequestBody Student student){
        studentRepositoryService.addNewStudent(student);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "{studentId}/delete")
    public void deleteStudent(@PathVariable("studentId") String studentId){
        studentRepositoryService.deleteStudent(studentId);
    }

    @PreAuthorize("hasRole('ROLE_student')")
    @PutMapping(path = "/updatePass/{studentId}")
    public void updateStudentPassword(@PathVariable("studentId") String studentId, @Valid @RequestBody String newPassword){
        Optional<Student> optionalStudent = studentRepositoryService.getStudent(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setPassword(newPassword);
            studentRepositoryService.update(student);
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/update/{studentId}")
    public void updateStudent(@PathVariable("studentId") String studentId, @Valid @RequestBody Student student){
        student.setId(studentId);
        studentRepositoryService.update(student);
    }

    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    @PutMapping(path = "/update/{studentId}/enroll")
    public void enrollStudent(@PathVariable("studentId") String studentId, @Valid @RequestBody List<Course> courseList){
        Optional<Student> optionalStudent = studentRepositoryService.getStudent(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            List<String> ids = student.getCourseIds();
            if (ids == null) {
                ids = new ArrayList<>();
            }
            for(int i=0; i<courseList.size(); i++){
                ids.add(courseList.get(i).getId());
                courseRepositoryService.addStudentToCourse(courseList.get(i).getId(), studentId);
            }
            student.setCourseIds(ids);
            studentRepositoryService.update(student);
        } else {
            return;
        }
    }

}
