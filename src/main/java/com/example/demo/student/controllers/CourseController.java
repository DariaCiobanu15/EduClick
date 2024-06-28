package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.*;
import com.example.demo.student.services.booking.BookingRepositoryService;
import com.example.demo.student.services.course.CourseRepositoryService;
import com.example.demo.student.services.post.PostRepositoryService;
import com.example.demo.student.services.student.StudentRepositoryService;
import com.example.demo.student.services.studyHall.StudyHallRepositoryService;
import com.example.demo.student.services.teacher.TeacherRepositoryService;
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
@RequestMapping(path = "api/v1/student/course")
public class CourseController {
    private final CourseRepositoryService courseRepositoryService;
    private final StudentRepositoryService studentRepositoryService;
    private final TeacherRepositoryService teacherRepositoryService;
    private final PostRepositoryService postRepositoryService;
    private final BookingRepositoryService bookingRepositoryService;
    private final StudyHallRepositoryService studyHallRepositoryService;

    @Autowired
    public CourseController(CourseRepositoryService courseRepositoryService,
                            StudentRepositoryService studentRepositoryService,
                            TeacherRepositoryService teacherRepositoryService,
                            PostRepositoryService postRepositoryService,
                            BookingRepositoryService bookingRepositoryService,
                            StudyHallRepositoryService studyHallRepositoryService) {
        this.courseRepositoryService = courseRepositoryService;
        this.studentRepositoryService = studentRepositoryService;
        this.teacherRepositoryService = teacherRepositoryService;
        this.postRepositoryService = postRepositoryService;
        this.bookingRepositoryService = bookingRepositoryService;
        this.studyHallRepositoryService = studyHallRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<Course> getCourses(){
        return courseRepositoryService.getCourses();
    }

    @GetMapping(path = "/{courseId}")
    public Optional<Course> getCourse(@PathVariable("courseId") String courseId){
        return courseRepositoryService.getCourse(courseId);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/add")
    public Course registerNewCourse(@RequestBody Course course){
        courseRepositoryService.addNewCourse(course);
        return course;
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "{courseId}/delete")
    public void deleteCourse(@PathVariable("courseId") String courseId){
        courseRepositoryService.deleteCourse(courseId);
        List<Student> students = studentRepositoryService.getStudents();
        for (Student student : students) {
            if (student.getCourseIds().contains(courseId)) {
                student.getCourseIds().remove(courseId);
                studentRepositoryService.update(student);
            }
            List<String> activitiesIds = student.getActivitiesIds();
            for (String activityId : activitiesIds) {
                Optional<Post> activity = postRepositoryService.getPost(activityId);
                if(activity.isPresent()) {
                    if (activity.get().getCourseId().equals(courseId)) {
                        student.getActivitiesIds().remove(activityId);
                        studentRepositoryService.update(student);
                    }
                }
            }
        }
        List<Teacher> teachers = teacherRepositoryService.getTeachers();
        for (Teacher teacher : teachers) {
            if (teacher.getCourseIds().contains(courseId)) {
                teacher.getCourseIds().remove(courseId);
                teacherRepositoryService.update(teacher);
            }
            if(teacher.getLabIds().contains(courseId)){
                teacher.getLabIds().remove(courseId);
                teacherRepositoryService.update(teacher);
            }
        }
        List<Booking> bookings = bookingRepositoryService.getBookings();
        for (Booking booking : bookings) {
            if (booking.getCourseId().equals(courseId)) {
                bookingRepositoryService.deleteBooking(booking.getId());
                List<StudyHall> studyHalls = studyHallRepositoryService.getStudyHalls();
                for (StudyHall studyHall : studyHalls) {
                    if (studyHall.getBookingIds().contains(booking.getId())) {
                        studyHall.getBookingIds().remove(booking.getId());
                        studyHallRepositoryService.update(studyHall);
                    }
                }
            }
        }
        List<Post> posts = postRepositoryService.getPosts();
        for (Post post : posts) {
            if (post.getCourseId().equals(courseId)) {
                postRepositoryService.deletePost(post.getId());
            }
        }
    }
    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    @PutMapping(path = "/update/{courseId}")
    public void updateCourse(@PathVariable("courseId") String courseId, @Valid @RequestBody Course course){
        course.setId(courseId);
        courseRepositoryService.update(course);
    }

    @PreAuthorize("hasRole('ROLE_teacher')")
    @PutMapping(path = "/updateDescription/{courseId}")
    public void updateDescription(@PathVariable("courseId") String courseId, @Valid @RequestBody String description){
        System.out.println(description);
        Optional<Course> optionalCourse = courseRepositoryService.getCourse(courseId);
        if(optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setDescription(description);
            courseRepositoryService.update(course);
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/assignTeacher/{courseId}/{teacherId}")
    public void assignTeacherToCourse(@PathVariable String courseId, @PathVariable String teacherId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        Optional<Teacher> teacherOptional = teacherRepositoryService.getTeacher(teacherId);

        if (courseOptional.isPresent() && teacherOptional.isPresent()) {
            Course course = courseOptional.get();
            course.setTeacherId(teacherId);
            courseRepositoryService.update(course);
        }
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/assignLabTeachers/{courseId}/{teacherId}")
    public void assignLabTeachersToCourse(@PathVariable String courseId, @PathVariable String teacherId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        Optional<Teacher> teacherOptional = teacherRepositoryService.getTeacher(teacherId);
        courseOptional.ifPresent(course -> {
            if (teacherOptional.isPresent()) {
                Teacher teacher = teacherOptional.get();
                List<String> labTeachers = course.getLabTeacherIds();
                labTeachers.add(teacherId);
                course.setLabTeacherIds(labTeachers);
                courseRepositoryService.update(course);
            }
        });
    }

    @PreAuthorize("hasRole('ROLE_admin') || hasRole('ROLE_teacher')")
    @PutMapping(path = "/enroll/{studentId}")
    @Transactional
    public void enrollStudentToCourse(@Valid @RequestBody Course course, @PathVariable String studentId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(course.getId());
        System.out.println(studentId);
        if (courseOptional.isPresent()) {
            Course c = courseOptional.get();
            System.out.println(c);
            ArrayList<String> students = (ArrayList<String>) c.getStudentsIds();
            if (students.contains(studentId)) {
                throw new IllegalStateException("student already in course");
            }
            students.add(studentId);
            c.setStudentsIds(students);
            System.out.println(c.getStudentsIds());
            courseRepositoryService.update(c);
        }
    }


    @GetMapping(path = "/{courseId}/posts")
    public ResponseEntity<List<Post>> getPostsByCourse(@PathVariable String courseId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        List<String> postIds = course.getPostsIds();
        List<Post> posts = new ArrayList<>();

        for (String postId : postIds) {
            Optional<Post> post = postRepositoryService.getPost(postId);
            post.ifPresent(posts::add);
        }

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping(path = "/{courseId}/bookings")
    public ResponseEntity<List<Booking>> getBookingsByCourse(@PathVariable String courseId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        List<String> bookingIds = course.getBookingIds();
        List<Booking> bookings = new ArrayList<>();

        for (String bookingId : bookingIds) {
            Optional<Booking> booking = bookingRepositoryService.getBooking(bookingId);
            booking.ifPresent(bookings::add);
        }

        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping(path = "/{courseId}/courseBooking")
    public ResponseEntity<Booking> getFirstBookingByCourse(@PathVariable String courseId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        List<String> bookingIds = course.getBookingIds();
        if (bookingIds.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Booking> booking = bookingRepositoryService.getBooking(bookingIds.get(0));
        return booking.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/{courseId}/teacher")
    public ResponseEntity<Teacher> getTeacherByCourse(@PathVariable String courseId) {
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        Optional<Teacher> teacher = teacherRepositoryService.getTeacher(course.getTeacherId());

        return teacher.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/{courseId}/studyHall")
    public ResponseEntity<String> getStudyHallNameByCourseId(@PathVariable("courseId") String courseId){
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        Optional<Booking> booking = bookingRepositoryService.getBooking(course.getBookingIds().get(0));
        if (!booking.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<StudyHall> studyHall = studyHallRepositoryService.getStudyHall(booking.get().getStudyHallId());
        if (!studyHall.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(studyHall.get().getName(), HttpStatus.OK);
    }

    @GetMapping(path = "/{courseId}/myLab/{subgroup}")
    public ResponseEntity<Booking> getLabBookingBySubgroup(@PathVariable("courseId") String courseId, @PathVariable("subgroup") String subgroup) {

        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Course course = courseOptional.get();
        List<String> bookingIds = course.getBookingIds();
        for (String bookingId : bookingIds) {
            Optional<Booking> booking = bookingRepositoryService.getBooking(bookingId);
            if (booking.isPresent()) {
                if (subgroup.equals(booking.get().getSubGroup())) {
                    System.out.println("Returning booking ID: " + booking.get().getId());
                    return new ResponseEntity<>(booking.get(), HttpStatus.OK);
                }
            }
        }

        System.out.println("No match found for subgroup: " + subgroup);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/{id}/myCourses")
    public List<String> getMyCourses(@PathVariable("id") String id) {
        List<Course> courses = courseRepositoryService.getCourses();
        List<String> myCourses = new ArrayList<>();
        for (Course course : courses) {
            if (course.getStudentsIds().contains(id)) {
                myCourses.add(course.getId());
            }
            if(course.getTeacherId().equals(id)) {
                myCourses.add(course.getId());
            }
        }
        return myCourses;
    }

    @GetMapping(path = "/{courseId}/getName")
    public ResponseEntity<String> getCourseName(@PathVariable("courseId") String courseId){
        Optional<Course> courseOptional = courseRepositoryService.getCourse(courseId);
        if (!courseOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(courseOptional.get().getName(), HttpStatus.OK);
    }

}
