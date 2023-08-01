package com.example.demo.student;

import com.example.demo.student.services.StudentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private final StudentRepositoryService studentRepositoryService;

    @Autowired
    public StudentController(StudentRepositoryService studentRepositoryService) {
        this.studentRepositoryService = studentRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Student> getStudents(){
        return studentRepositoryService.getStudents();
    }
    @GetMapping(path = "{studentId}")
    public Optional<Student> getStudent(@PathVariable("studentId") String studentId){
        return studentRepositoryService.getStudent(studentId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewStudent(@RequestBody Student student){
        studentRepositoryService.addNewStudent(student);
    }

    @DeleteMapping(path = "{studentId}/delete")
    public void deleteStudent(@PathVariable("studentId") String studentId){
        studentRepositoryService.deleteStudent(studentId);
    }
    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") String studentId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String group,
            @RequestParam(required = false) Integer age){
        studentRepositoryService.updateStudent(studentId, userName, firstName, lastName, year, group, age);
    }

}
