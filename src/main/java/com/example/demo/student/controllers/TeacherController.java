package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Booking;
import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.StudyHall;
import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.services.booking.BookingRepositoryService;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.studyHall.StudyHallRepositoryService;
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
    private final CourseRepositoryService courseRepositoryService;
    private final BookingRepositoryService bookingRepositoryService;
    private final StudyHallRepositoryService studyHallRepositoryService;

    @Autowired
    public TeacherController(TeacherRepositoryService teacherRepositoryService, CourseRepositoryService courseRepositoryService, BookingRepositoryService bookingRepositoryService, StudyHallRepositoryService studyHallRepositoryService) {
        this.teacherRepositoryService = teacherRepositoryService;
        this.courseRepositoryService = courseRepositoryService;
        this.bookingRepositoryService = bookingRepositoryService;
        this.studyHallRepositoryService = studyHallRepositoryService;
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
        Teacher teacher = teacherRepositoryService.getTeacher(teacherId)
                .orElseThrow(() -> new IllegalStateException("teacher with id " + teacherId + " does not exist"));

        if (teacher.getCourseIds() != null) {
            for (String courseId : teacher.getCourseIds()) {
                Optional<Course> courseOpt = courseRepositoryService.getCourse(courseId);
                if (courseOpt.isPresent()) {
                    Course course = courseOpt.get();
                    course.setTeacherId(null);
                    courseRepositoryService.update(course);
                }
            }
        }

        if (teacher.getLabIds() != null) {
            for (String labId : teacher.getLabIds()) {
                Optional<Course> labOpt = courseRepositoryService.getCourse(labId);
                if (labOpt.isPresent()) {
                    Course lab = labOpt.get();
                    lab.getLabTeacherIds().removeIf(id -> id.equals(teacherId));
                    courseRepositoryService.update(lab);
                }
            }
        }

        List<Booking> bookings = bookingRepositoryService.getBookings();
        for (Booking booking : bookings) {
            if (booking.getTeacherId().equals(teacherId)) {
                Optional<StudyHall> studyHallOpt = studyHallRepositoryService.getStudyHall(booking.getStudyHallId());
                if (studyHallOpt.isPresent()) {
                    StudyHall studyHall = studyHallOpt.get();
                    studyHall.getBookingIds().remove(booking.getId());
                    studyHallRepositoryService.update(studyHall);
                    bookingRepositoryService.deleteBooking(booking.getId());
                }
            }
        }

        teacherRepositoryService.deleteTeacher(teacherId);
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

    @GetMapping(path = "/{teacherId}/getMyCourses")
    public List<Course> getMyCourses(@PathVariable("teacherId") String teacherId){
        Optional<Teacher> optionalTeacher = teacherRepositoryService.getTeacher(teacherId);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            List<String> ids = teacher.getCourseIds();
            List<Course> courses = new ArrayList<>();
            for (String id : ids) {
                Optional<Course> course = courseRepositoryService.getCourse(id);
                if (course.isPresent()) {
                    courses.add(course.get());
                }
            }
            return courses;
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
