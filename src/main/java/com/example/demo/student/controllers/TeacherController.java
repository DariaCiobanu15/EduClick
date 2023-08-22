package com.example.demo.student.controllers;

import com.example.demo.student.services.TeacherRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/teacher")
public class TeacherController {
    private final TeacherRepositoryService teacherRepositoryService;

    @Autowired
    public TeacherController(TeacherRepositoryService teacherRepositoryService) {
        this.teacherRepositoryService = teacherRepositoryService;
    }


}
