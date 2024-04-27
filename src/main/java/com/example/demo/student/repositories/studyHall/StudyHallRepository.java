package com.example.demo.student.repositories.studyHall;

import com.example.demo.student.componentObj.StudyHall;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudyHallRepository extends CrudRepository<StudyHall, String> {
    Optional<StudyHall> findById(String studyHallId);
    List<StudyHall> findAllByCapacityGreaterThanEqual(Integer capacity);
}
