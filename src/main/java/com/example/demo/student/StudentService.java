package com.example.demo.student;

import com.couchbase.client.core.api.search.queries.CoreGeoCoordinates;
import com.couchbase.client.core.deps.com.google.common.base.Optional;

import java.util.List;

public interface StudentService {
    Optional<Student> findOne(String id);
    List<Student> findAll();
    List<Student> findByName(String firstName);
    void create(Student student);
    void update(Student student);
    void delete(Student student);
}
