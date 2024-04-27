package com.example.demo.student.services.booking;

import com.example.demo.student.componentObj.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    void create (Booking booking);
    void update (Booking booking);
    void delete (Booking booking);

    Optional<Booking> findOne(String id);
    List<Booking> findAll();

}
