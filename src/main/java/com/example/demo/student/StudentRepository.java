package com.example.demo.student;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends CrudRepository <Student, String>{

    List<Student> findByUserName(String userName);
    Optional<Student> findById(String studentId);
}
