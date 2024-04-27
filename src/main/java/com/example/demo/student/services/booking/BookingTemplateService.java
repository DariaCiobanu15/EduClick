package com.example.demo.student.services.booking;

import com.example.demo.student.componentObj.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;
import java.util.Optional;

public class BookingTemplateService implements BookingService {

    private static final String DESIGN_DOC = "booking";
    private CouchbaseTemplate template;

    @Override
    public void create(Booking booking) {
        template.insertById(Booking.class).one(booking);
    }

    @Override
    public void update(Booking booking) {
        template.removeById(Booking.class).one(booking.getId());
    }

    @Override
    public void delete(Booking booking) {
        template.removeById(Booking.class).one(booking.getId());
    }

    @Override
    public Optional<Booking> findOne(String id) {
        return Optional.of(template.findById(Booking.class).one(id));
    }

    @Override
    public List<Booking> findAll() {
        return template.findByQuery(Booking.class).all();
    }

    @Autowired
    public void setCouchbaseTemplate(CouchbaseTemplate template) {
        this.template = template;
    }
}

