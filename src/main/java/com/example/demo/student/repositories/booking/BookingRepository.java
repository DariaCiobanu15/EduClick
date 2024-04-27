package com.example.demo.student.repositories.booking;

import com.example.demo.student.componentObj.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends CrudRepository<Booking, String> {
    Optional<Booking> findById(String bookingId);
    List<Booking> findAllByStudyHallId(String id);
}
