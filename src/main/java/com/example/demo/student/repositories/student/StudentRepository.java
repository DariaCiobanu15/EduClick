package com.example.demo.student.repositories.student;
import com.example.demo.student.componentObj.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends CrudRepository <Student, String>{

    List<Student> findByUsername(String username);
    Optional<Student> findById(String studentId);
}
