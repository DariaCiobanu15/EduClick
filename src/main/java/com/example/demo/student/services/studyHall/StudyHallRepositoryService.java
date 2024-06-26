package com.example.demo.student.services.studyHall;

import com.example.demo.student.componentObj.StudyHall;
import com.example.demo.student.repositories.studyHall.StudyHallRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Qualifier("StudyHallRepositoryService")
public class StudyHallRepositoryService {
    private final StudyHallRepository studyHallRepository;

    @Autowired
    public StudyHallRepositoryService (StudyHallRepository studyHallRepository) {
        this.studyHallRepository = studyHallRepository;
    }

    public Optional<StudyHall> getStudyHall (String id) {
        return studyHallRepository.findById(id);
    }

    public void createStudyHall (StudyHall studyHall) {
        studyHallRepository.save(studyHall);
    }
    public void deleteStudyHall (StudyHall studyHall) {
        studyHallRepository.delete(studyHall);
    }
    public void updateStudyHall (StudyHall studyHall) {
        studyHallRepository.save(studyHall);
    }

    public void deleteStudyHallById (String id) {
        boolean exists = studyHallRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("Study Hall with id " + id + " does not exist");
        }
        studyHallRepository.deleteById(id);
    }

    public void addNewStudyHall (StudyHall studyHall) {
        List<StudyHall> existing_studyHalls = (List<StudyHall>) studyHallRepository.findAll();
        List<String> studyHall_names = new ArrayList<String>();
        for (StudyHall s: existing_studyHalls) {
            studyHall_names.add(s.getName());
        }
        if (studyHall_names.contains(studyHall.getName())) {
            throw new IllegalStateException("Study Hall's name taken");
        } else {
            List<String> bookings = new ArrayList<String>();
            studyHall.setBookingIds(bookings);
            studyHallRepository.save(studyHall);
        }
    }


    public List<StudyHall> getStudyHalls() {
        return (List<StudyHall>) studyHallRepository.findAll();
    }

    public List<StudyHall> getAllByCapacityGreaterThanEqual(int size) {
        return studyHallRepository.findAllByCapacityGreaterThanEqual(size);
    }

    public void addBookingToStudyHall(String id, String id1) {
        Optional<StudyHall> studyHall = studyHallRepository.findById(id);
        if (studyHall.isPresent()) {
            StudyHall s = studyHall.get();
            if (s.getBookingIds() == null) {
                s.setBookingIds(new ArrayList<String>());
            }
            s.getBookingIds().add(id1);
            studyHallRepository.save(s);
        }
    }

    public String getName(String studyHallId) {
        Optional<StudyHall> studyHall = studyHallRepository.findById(studyHallId);
        if (studyHall.isPresent()) {
            return studyHall.get().getName();
        } else {
            throw new IllegalStateException("Study Hall with id " + studyHallId + " does not exist");
        }

    }

    public void update(StudyHall studyHall) {
        studyHallRepository.save(studyHall);
    }
}
