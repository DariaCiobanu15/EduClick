package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.services.teacher.TeacherRepositoryService;
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
            List<String> ids = teacher.getCourseIds();
            if (ids == null) {
                ids = new ArrayList<>();
            }
            for (Course course : courseList) {
                ids.add(course.getId());
            }
            teacher.setCourseIds(ids);
            teacherRepositoryService.update(teacher);

        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/update/{teacherId}/enrollToLab")
    public void enrollTeacherToLab(@PathVariable("teacherId") String teacherId, @Valid @RequestBody List<Course> labsList) {
        Optional<Teacher> optionalTeacher = teacherRepositoryService.getTeacher(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            List<String> ids = teacher.getLabIds();
            if (ids == null) {
                ids = new ArrayList<>();
            }
            for (Course lab : labsList) {
                ids.add(lab.getId());
            }
            teacher.setLabIds(ids);
            teacherRepositoryService.update(teacher);
        }
    }

    @GetMapping(path = "/{teacherId}/myCourses")
    public List<String> getCoursesFromTeacher(@PathVariable("teacherId") String teacherId){
        Optional<Teacher> optionalTeacher = teacherRepositoryService.getTeacher(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            List<String> ids = teacher.getCourseIds();
            System.out.println(ids);
            return ids;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/{teacherId}/myLabs")
    public List<String> getLabsFromTeacher(@PathVariable("teacherId") String teacherId){
        Optional<Teacher> optionalTeacher = teacherRepositoryService.getTeacher(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            List<String> ids = teacher.getLabIds();
            System.out.println(ids);
            return ids;
        } else {
            return null;
        }
    }

}
