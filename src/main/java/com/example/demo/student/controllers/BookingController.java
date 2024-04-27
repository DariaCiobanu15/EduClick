package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.Booking;
import com.example.demo.student.services.booking.BookingRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/booking")
public class BookingController {
    private final BookingRepositoryService bookingRepositoryService;

    @Autowired
    public BookingController(BookingRepositoryService bookingRepositoryService) {
        this.bookingRepositoryService = bookingRepositoryService;
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
    public void addNewBooking(@RequestBody Booking booking){
        bookingRepositoryService.create(booking);
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
