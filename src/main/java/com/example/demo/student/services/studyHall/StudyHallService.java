package com.example.demo.student.services.studyHall;

import com.example.demo.student.componentObj.StudyHall;

import java.util.List;
import java.util.Optional;

public interface StudyHallService {
    List<StudyHall> getAllStudyHalls();
    Optional<StudyHall> getStudyHallById(String id);
    void createStudyHall(StudyHall studyHall);
    void deleteStudyHall(StudyHall studyHall);
    void deleteStudyHallbyId(String id);
    void updateStudyHall(StudyHall studyHall);

}
