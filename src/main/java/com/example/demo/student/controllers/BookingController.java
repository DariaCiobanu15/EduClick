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

import java.util.*;

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
    public Booking addNewDynamicBooking(@RequestBody Booking booking) {
        Course course = courseRepositoryService.getCourse(booking.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        List<StudyHall> suitableHalls = studyHallRepositoryService.getAllByCapacityGreaterThanEqual(course.getStudentsIds().size());
        suitableHalls.sort(Comparator.comparingInt(StudyHall::getCapacity));
        String teacherId = course.getTeacherId();
        Teacher teacher = teacherRepositoryService.getTeacher(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));

        String[] weekdays = {"Luni", "Marți", "Miercuri", "Joi", "Vineri"};
        for (String weekday : weekdays) {
            for (StudyHall hall : suitableHalls) {
                for (int hour = 8; hour < 18; hour=hour+2) {
                    if (isHallAvailable(hall, weekday, hour) && isTeacherAvailable(teacher, weekday, hour)
                            && isGroupFromYearAvailable(course.getGroup(), course.getYear(), weekday, hour) &&
                            isSubGroupAvailable(course.getYear().toString() + course.getGroup() + '1', weekday, hour)
                            && isSubGroupAvailable(course.getYear().toString() + course.getGroup() + '2', weekday, hour)
                            && isSubGroupAvailable(course.getYear().toString() + course.getGroup() + '3', weekday, hour)) {
                        booking.setStudyHallId(hall.getId());
                        booking.setTeacherId(teacherId);
                        booking.setHour(hour);
                        booking.setWeekday(weekday);

                        bookingRepositoryService.create(booking);
//                        System.out.println(booking.getId());
                        courseRepositoryService.addBookingToCourse(booking.getCourseId(), booking.getId());
                        studyHallRepositoryService.addBookingToStudyHall(hall.getId(), booking.getId());
                        return booking;
                    }
                }
            }
        }
        throw new IllegalArgumentException("No suitable hall found");
    }

    @PostMapping(path = "/addDynamicLab")
    @PreAuthorize("hasRole('ROLE_admin')")
    public Booking addNewDynamicLabBooking(@RequestBody Booking booking) {
        System.out.println("Booking body:");
        System.out.println(booking);
        Course course = courseRepositoryService.getCourse(booking.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        List<StudyHall> suitableHalls = studyHallRepositoryService.getAllByCapacityGreaterThanEqual(course.getStudentsIds().size());
        suitableHalls.sort(Comparator.comparingInt(StudyHall::getCapacity));
        Integer year = course.getYear();
        String group = course.getGroup();
//        System.out.println(group);
        String labTeacher = booking.getTeacherId();
        Teacher teacher = teacherRepositoryService.getTeacher(labTeacher)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID"));
        String subGroup = booking.getSubGroup();

        String[] weekdays = {"Luni", "Marți", "Miercuri", "Joi", "Vineri"};
        for (String weekday : weekdays) {
            for (StudyHall hall : suitableHalls) {
                for (int hour = 8; hour < 18; hour += 2) {
                    if (isHallAvailable(hall, weekday, hour) && isTeacherAvailable(teacher, weekday, hour)
                            && isSubGroupAvailable(subGroup, weekday, hour) && isGroupFromYearAvailable(group, year, weekday, hour)) {
                        booking.setStudyHallId(hall.getId());
                        booking.setHour(hour);
                        booking.setWeekday(weekday);
                        booking.setLab(true);

                        bookingRepositoryService.create(booking);
//                        System.out.println("Created booking: ");
//                        System.out.println(booking);
                        courseRepositoryService.addBookingToCourse(booking.getCourseId(), booking.getId());
                        studyHallRepositoryService.addBookingToStudyHall(hall.getId(), booking.getId());
                        return booking;
                    } else {
//                        System.out.println("No suitable hall found");
                    }
                }
            }
        }
        throw new IllegalArgumentException("No suitable hall found");
    }


    private boolean isHallAvailable(StudyHall hall, String weekday, int hour) {
        System.out.println(hall);
        if (hall.getBookingIds().size() == 0) {
            System.out.println("Hall is empty");
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
            System.out.println("Teacher is empty");
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

    private boolean isYearAvailable(Integer year, String weekday, int hour) {
        List<Course> courses = courseRepositoryService.getCoursesByYear(year);
        for (Course course : courses) {
            if (course.getBookingIds() == null || course.getBookingIds().size() == 0) {
                continue;
            }
            for (String bookingId : course.getBookingIds()) {
                Booking booking = bookingRepositoryService.getBooking(bookingId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
                if (booking.getWeekday().equals(weekday) && booking.getHour() == hour) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGroupFromYearAvailable(String group, Integer year, String weekday, int hour) {
        List<Course> courses = courseRepositoryService.getCoursesByGroupAndYear(group, year);
        for (Course course : courses) {
            if (course.getBookingIds() == null || course.getBookingIds().size() == 0) {
                continue;
            }
            for (String bookingId : course.getBookingIds()) {
                Booking booking = bookingRepositoryService.getBooking(bookingId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
                if (booking.getWeekday().equals(weekday) && booking.getHour() == hour) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSubGroupAvailable(String subGroup, String weekday, int hour) {
        List<Booking> bookings = bookingRepositoryService.getBookings();
        for (Booking booking : bookings) {
            if(booking.getSubGroup() != null) {
                if (booking.getSubGroup().equals(subGroup) && booking.getWeekday().equals(weekday) && booking.getHour() == hour) {
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

    @GetMapping(path = "/{courseId}/{teacherId}/getLabBookingsForTeacher")
    public List<Booking> getLabBookingsForTeacher(@PathVariable("courseId") String courseId, @PathVariable("teacherId") String teacherId) {
        List<Booking> bookings = bookingRepositoryService.getBookings();
        if (bookings.isEmpty()) {
            return new ArrayList<>(); // Return an empty list instead of null
        }

        List<Booking> teacherBookings = new ArrayList<>(); // Initialize a mutable list
        for (Booking booking : bookings) {
            if (booking.getCourseId().equals(courseId) && booking.getTeacherId().equals(teacherId) && booking.isLab()) {
                teacherBookings.add(booking);
            }
        }
        System.out.println("Teacher bookings: ");
        System.out.println(teacherBookings);
        return teacherBookings;
    }

    @GetMapping(path = "/{bookingId}/getLabBookingDetails")
    public List<Map<String, String>> getLabBookingDetails(@PathVariable("bookingId") String bookingId) {
        Booking booking = bookingRepositoryService.getBooking(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));
        Course course = courseRepositoryService.getCourse(booking.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        StudyHall hall = studyHallRepositoryService.getStudyHall(booking.getStudyHallId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid study hall ID"));

        List<Map<String, String>> details = new ArrayList<>();
        Map<String, String> bookingDetails = new HashMap<>();
        bookingDetails.put("bookingId", booking.getId());
        bookingDetails.put("courseId", course.getId());
        bookingDetails.put("courseName", course.getName());
        bookingDetails.put("studyHallId", hall.getId());
        bookingDetails.put("studyHallName", hall.getName());
        bookingDetails.put("weekday", booking.getWeekday());
        bookingDetails.put("hour", booking.getHour().toString());
        bookingDetails.put("subGroup", booking.getSubGroup());
        details.add(bookingDetails);

        return details;
    }

}
