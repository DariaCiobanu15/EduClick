package com.example.demo.student;

import com.example.demo.student.services.StudentRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @PutMapping(path = "/update/{studentId}")
    public void updateStudent(@PathVariable("studentId") String studentId, @Valid @RequestBody Student student){
        student.setId(studentId);
        studentRepositoryService.update(student);
    }

}
