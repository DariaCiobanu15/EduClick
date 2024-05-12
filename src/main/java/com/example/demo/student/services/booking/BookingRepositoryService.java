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
