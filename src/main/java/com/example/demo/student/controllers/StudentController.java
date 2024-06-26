package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.*;
import com.example.demo.student.repositories.post.PostRepository;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.post.PostRepositoryService;
import com.example.demo.student.services.student.StudentRepositoryService;
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
@RequestMapping(path = "api/v1/student")

public class StudentController {

    private final StudentRepositoryService studentRepositoryService;
    private final CourseRepositoryService courseRepositoryService;

    private final PostRepositoryService postRepositoryService;
    @Autowired
    public StudentController(StudentRepositoryService studentRepositoryService, CourseRepositoryService courseRepositoryService, PostRepositoryService postRepositoryService) {
        this.studentRepositoryService = studentRepositoryService;
        this.courseRepositoryService = courseRepositoryService;
        this.postRepositoryService = postRepositoryService;
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
            System.out.println(ids);
            return ids;
        } else {
            return null;
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public void registerNewStudent(@RequestBody Student student){

        studentRepositoryService.addNewStudent(student);
        UniversityData data = student.getUniversityData();
        if (data != null) {
            int year = data.getYear();
            String group = data.getGroup();
            List<Course> courses = courseRepositoryService.getCourses();
            for (Course course : courses) {
                if (course.getYear() == year && course.getGroup().equals(group)) {
                    List<String> studentsIds = course.getStudentsIds();
                    if (studentsIds == null) {
                        studentsIds = new ArrayList<>();
                    }
                    studentsIds.add(student.getId());
                    course.setStudentsIds(studentsIds);
                    List<String> ids = student.getCourseIds();
                    if (ids == null) {
                        ids = new ArrayList<>();
                    }
                    ids.add(course.getId());
                    student.setCourseIds(ids);
                    studentRepositoryService.update(student);
                    courseRepositoryService.update(course);
                }
            }
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "{studentId}/delete")
    public void deleteStudent(@PathVariable("studentId") String studentId){
        studentRepositoryService.deleteStudent(studentId);

        List<Course> courses = courseRepositoryService.getCourses();
        for (Course course : courses) {
            List<String> studentsIds = course.getStudentsIds();
            if (studentsIds != null) {
                studentsIds.remove(studentId);
                course.setStudentsIds(studentsIds);
                courseRepositoryService.update(course);
            }
            List<Post> posts = postRepositoryService.getPostsByCourseId(course.getId());
            for (Post post : posts) {
                List<StudentUploadedContent> studentUploadedContents = post.getStudentUploadedContents();
                if (studentUploadedContents != null) {
                    for (StudentUploadedContent studentUploadedContent : studentUploadedContents) {
                        if (studentUploadedContent.getStudentId().equals(studentId)) {
                            studentUploadedContents.remove(studentUploadedContent);
                        }
                    }
                    post.setStudentUploadedContents(studentUploadedContents);
                    postRepositoryService.update(post);
                }
            }
        }
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
    @Transactional
    public void enrollStudent(@PathVariable("studentId") String studentId, @Valid @RequestBody List<Course> courseList){
        Optional<Student> optionalStudent = studentRepositoryService.getStudent(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            List<String> ids = student.getCourseIds();
            if (ids == null) {
                ids = new ArrayList<>();
            }
            for (Course course : courseList) {
                if (ids.contains(course.getId())) {
                    throw new IllegalStateException("student already in course");
                } else {
                    ids.add(course.getId());
                }
            }
            student.setCourseIds(ids);
            studentRepositoryService.update(student);
        } else {
            return;
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/update/enrollYear")
    public void enrollYearToCourse(@Valid @RequestBody Course course){
        List<Student> students = studentRepositoryService.getStudents();
        List<String> studentsIds = course.getStudentsIds();
        if(studentsIds == null) {
            studentsIds = new ArrayList<>();
        }
        System.out.println(students);
        int year = course.getYear();
        for (Student student : students) {
            UniversityData data = student.getUniversityData();
            if (data.getYear() == year) {
                List<String> ids = student.getCourseIds();
                if (ids == null) {
                    ids = new ArrayList<>();
                }
                ids.add(course.getId());
                student.setCourseIds(ids);
                System.out.println(student.getCourseIds());
                studentsIds.add(student.getId());
                studentRepositoryService.update(student);
            }
        }
        course.setStudentsIds(studentsIds);
        courseRepositoryService.update(course);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/update/enrollGroupFromYear")
    public void enrollGroupFromYearToCourse(@Valid @RequestBody Course course){
        List<Student> students = studentRepositoryService.getStudents();
        List<String> studentsIds = course.getStudentsIds();
        if(studentsIds == null) {
            studentsIds = new ArrayList<>();
        }
        System.out.println(students);
        int year = course.getYear();
        String group = course.getGroup();
        for (Student student : students) {
            UniversityData data = student.getUniversityData();
            if (data.getYear() == year && data.getGroup().equals(group)) {
                List<String> ids = student.getCourseIds();
                if (ids == null) {
                    ids = new ArrayList<>();
                }
                ids.add(course.getId());
                student.setCourseIds(ids);
                System.out.println(student.getCourseIds());
                studentsIds.add(student.getId());
                studentRepositoryService.update(student);
            }
        }
        course.setStudentsIds(studentsIds);
        courseRepositoryService.update(course);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(path = "/addMany")
    public ResponseEntity<?> addManyStudents(@RequestBody List<Student> students){
        for (Student student : students) {
            studentRepositoryService.addNewStudent(student);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{studentId}/subGroup")
    public ResponseEntity<?> getSubGroup(@PathVariable("studentId") String studentId) {
        Optional<Student> optionalStudent = studentRepositoryService.getStudent(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            UniversityData data = student.getUniversityData();
            String group = data.getGroup();
            String subGroup = data.getSubGroup();
            return ResponseEntity.ok(subGroup);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



}
