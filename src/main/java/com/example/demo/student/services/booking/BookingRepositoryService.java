package com.example.demo.student.services.booking;

import com.example.demo.student.componentObj.Booking;
import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.StudyHall;
import com.example.demo.student.repositories.booking.BookingRepository;
import com.example.demo.student.repositories.course.CourseRepository;
import com.example.demo.student.repositories.studyHall.StudyHallRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Qualifier("BookingRepositoryService")
public class BookingRepositoryService {
    private final BookingRepository bookingRepository;
    private final StudyHallRepository studyHallRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public BookingRepositoryService(BookingRepository bookingRepository, StudyHallRepository studyHallRepository, CourseRepository courseRepository) {
        this.bookingRepository = bookingRepository;
        this.studyHallRepository = studyHallRepository;
        this.courseRepository = courseRepository;
    }

    public void create(Booking booking) {
        bookingRepository.save(booking);
    }

    public Booking createBookingForCourse(String courseId, String weekday, Integer hour) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalArgumentException("Invalid course ID"));
        // Sorting all the suitable studyHalls to find the smallest one that could fit the students
        List<StudyHall> suitableHalls = studyHallRepository.findAllByCapacityGreaterThanEqual(course.getStudentsIds().size());
        suitableHalls.sort(Comparator.comparing(StudyHall::getCapacity));

        for (StudyHall hall : suitableHalls) {
            if (isHallAvailable(hall, weekday, hour)) {
                Booking booking = new Booking();
                booking.setCourseId(courseId);
                booking.setStudyHallId(hall.getId());
                booking.setWeekday(weekday);
                booking.setHour(hour);
                bookingRepository.save(booking);

                updateCourseAndStudyHallWithBooking(course, hall, booking);
                return booking;
            }
        }
        throw new IllegalStateException("No available study hall found");
    }

    private void updateCourseAndStudyHallWithBooking(Course course, StudyHall hall, Booking booking) {
        course.getBookingIds().add(booking.getId());
        courseRepository.save(course);

        hall.getBookingIds().add(booking.getId());
        studyHallRepository.save(hall);
    }

    private boolean isHallAvailable(StudyHall hall, String weekday, Integer hour) {
        List<Booking> bookings = bookingRepository.findAllByStudyHallId(hall.getId());

        // Check if there's any booking that conflicts with the desired weekday and hour.
        for (Booking booking : bookings) {
            if (booking.getWeekday().equalsIgnoreCase(weekday) && booking.getHour().equals(hour)) {
                // Found a conflicting booking, so the hall is not available.
                return false;
            }
        }

        // No conflicting bookings found, so the hall is available.
        return true;
    }

    public void update(Booking booking) {
        bookingRepository.save(booking);
    }
    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }
    public List<Booking> getBookings() {
        return (List<Booking>) bookingRepository.findAll();
    }

    public void deleteBooking(String id) {
        boolean exists = bookingRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("Post doesn't exist!");
        }
        bookingRepository.deleteById(id);
    }

    public Optional<Booking> getBooking(String bookingId) {
        return bookingRepository.findById(bookingId);
    }
}
