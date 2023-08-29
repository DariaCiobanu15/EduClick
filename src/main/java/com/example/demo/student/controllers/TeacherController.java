package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Student;
import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.services.TeacherRepositoryService;
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
@RequestMapping(path = "api/v1/student/teacher")
public class TeacherController {
    private final TeacherRepositoryService teacherRepositoryService;

    @Autowired
    public TeacherController(TeacherRepositoryService teacherRepositoryService) {
        this.teacherRepositoryService = teacherRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Teacher> getTeachers(){
        return teacherRepositoryService.getTeachers();
    }

    @GetMapping(path = "{teacherId}")
    public Optional<Teacher> getTeacher(@PathVariable("teacherId") String teacherId) {
        return teacherRepositoryService.getTeacher(teacherId);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewTeacher(@RequestBody Teacher teacher){
        teacherRepositoryService.addNewTeacher(teacher);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "{teacherId}/delete")
    public void deleteTeacher(@PathVariable("teacherId") String teacherId) {
        teacherRepositoryService.deleteCourse(teacherId);
    }
    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "{teacherId}/update")
    public void updateTeacher(@PathVariable("teacherId") String teacherId, @Valid @RequestBody Teacher teacher) {
        teacher.setId(teacherId);
        teacherRepositoryService.update(teacher);
    }

    @PreAuthorize("hasRole('ROLE_teacher')")
    @PutMapping(path = "/updatePass/{teacherId}")
    public void updateTeacherPassword(@PathVariable("teacherId") String teacherId, @Valid @RequestBody String newPassword){
        Optional<Teacher> optionalTeacher = teacherRepositoryService.getTeacher(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            teacher.setPassword(newPassword);
            teacherRepositoryService.update(teacher);
        }
    }
    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/update/{teacherId}/enroll")
    public void enrollTeacher(@PathVariable("teacherId") String teacherId, @Valid @RequestBody List<Course> courseList) {
        Optional<Teacher> optionalTeacher = teacherRepositoryService.getTeacher(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            List<String> ids = new ArrayList<>();
            for(int i=0; i<courseList.size(); i++) {
                ids.add(courseList.get(i).getId());
            }
            teacher.setCourseIds(ids);
            teacherRepositoryService.update(teacher);
        }
    }

}
