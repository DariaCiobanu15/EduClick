package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Booking;
import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.StudyHall;
import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.repositories.studyHall.StudyHallRepository;
import com.example.demo.student.services.booking.BookingRepositoryService;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.student.StudentRepositoryService;
import com.example.demo.student.services.studyHall.StudyHallRepositoryService;
import com.example.demo.student.services.teacher.TeacherRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/booking")
public class BookingController {
    private final BookingRepositoryService bookingRepositoryService;
    private final CourseRepositoryService courseRepositoryService;
    private final StudyHallRepositoryService studyHallRepositoryService;
    private final TeacherRepositoryService teacherRepositoryService;

    @Autowired
    public BookingController(BookingRepositoryService bookingRepositoryService, CourseRepositoryService courseRepositoryService, StudyHallRepositoryService studyHallRepositoryService, TeacherRepositoryService teacherRepositoryService) {
        this.bookingRepositoryService = bookingRepositoryService;
        this.courseRepositoryService = courseRepositoryService;
        this.studyHallRepositoryService = studyHallRepositoryService;
        this.teacherRepositoryService = teacherRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Booking> getBookings(){
        return bookingRepositoryService.getBookings();
    }

    @GetMapping(path = "/{bookingId}")
    public Optional<Booking> getBooking(@PathVariable("bookingId") String bookingId){
        return bookingRepositoryService.getBooking(bookingId);
    }

    @PostMapping(path = "/add")
    @PreAuthorize("hasRole('ROLE_admin')")
    public void addNewBooking(@RequestBody Booking booking){
        bookingRepositoryService.create(booking);
        courseRepositoryService.addBookingToCourse(booking.getCourseId(), booking.getId());
    }

    @PostMapping(path = "/addDynamic")
    @PreAuthorize("hasRole('ROLE_admin')")
    public void addNewDynamicBooking(@RequestBody Booking booking) {
        Course course = courseRepositoryService.getCourse(booking.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        List<StudyHall> suitableHalls = studyHallRepositoryService.getAllByCapacityGreaterThanEqual(course.getStudentsIds().size());
        suitableHalls.sort(Comparator.comparingInt(StudyHall::getCapacity).reversed());
        String teacherId = course.getTeacherId();
        Teacher teacher = teacherRepositoryService.getTeacher(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));

        String[] weekdays = {"Luni", "Marți", "Miercuri", "Joi", "Vineri"};
        for (String weekday : weekdays) {
            for (StudyHall hall : suitableHalls) {
                for (int hour = 8; hour < 18; hour++) {
                    if (isHallAvailable(hall, weekday, hour) && isTeacherAvailable(teacher, weekday, hour)) {
                        booking.setStudyHallId(hall.getId());
                        booking.setTeacherId(teacherId);
                        booking.setHour(hour);
                        booking.setWeekday(weekday);

                        bookingRepositoryService.create(booking);
                        courseRepositoryService.addBookingToCourse(booking.getCourseId(), booking.getId());
                        return;
                    }
                }
            }
        }
        throw new IllegalArgumentException("No suitable hall found");
    }

    private boolean isHallAvailable(StudyHall hall, String weekday, int hour) {
        if (hall.getBookingIds().size() == 0) {
            return true;
        } else {
            for (String bookingId : hall.getBookingIds()) {
                Booking booking = bookingRepositoryService.getBooking(bookingId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
                if (booking.getWeekday().equals(weekday) && booking.getHour() == hour) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean isTeacherAvailable(Teacher teacher, String weekday, int hour) {
        if(teacher.getCourseIds().size() == 0 && teacher.getLabIds().size() == 0) {
            return true;
        }
        for (String courseId : teacher.getCourseIds()) {
            Course course = courseRepositoryService.getCourse(courseId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
            if(course.getBookingIds() == null || course.getBookingIds().size() == 0) {
                return true;
            }
            for(String courseBookingId : course.getBookingIds()) {
                Booking courseBooking = bookingRepositoryService.getBooking(courseBookingId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
                if (courseBooking.getWeekday().equals(weekday) && courseBooking.getHour() == hour) {
                    return false;
                }
            }
        }
        for (String labId : teacher.getLabIds()) {
            StudyHall lab = studyHallRepositoryService.getStudyHall(labId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid lab ID"));
            if(lab.getBookingIds().size() == 0) {
                return true;
            }
            for(String labBookingId : lab.getBookingIds()) {
                Booking labBooking = bookingRepositoryService.getBooking(labBookingId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
                if (labBooking.getWeekday().equals(weekday) && labBooking.getHour() == hour) {
                    return false;
                }
            }
        }
        return true;
    }


    public void updateBooking(@PathVariable("bookingId") String bookingId, @RequestBody Booking booking){
        booking.setId(bookingId);
        bookingRepositoryService.update(booking);
    }

    @DeleteMapping(path = "/{bookingId}/delete")
    public void deleteBooking(@PathVariable("bookingId") String bookingId){
        bookingRepositoryService.deleteBooking(bookingId);
    }



}
