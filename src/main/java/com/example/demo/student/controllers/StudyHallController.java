package com.example.demo.student.controllers;

import com.example.demo.student.componentObj.StudyHall;
import com.example.demo.student.services.studyHall.StudyHallRepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "api/v1/student/studyhall")
public class StudyHallController {

    private final StudyHallRepositoryService studyHallRepositoryService;

    public StudyHallController(StudyHallRepositoryService studyHallRepositoryService) {
        this.studyHallRepositoryService = studyHallRepositoryService;
    }

    @GetMapping(path = "/all")
    public List<StudyHall> getStudyHalls(){
        return studyHallRepositoryService.getStudyHalls();
    }

    @GetMapping(path = "/{studyHallId}")
    public Optional<StudyHall> getStudyHall(@PathVariable("studyHallId") String studyHallId){
        return studyHallRepositoryService.getStudyHall(studyHallId);
    }


    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewStudyHall(@RequestBody StudyHall studyHall) {
        studyHallRepositoryService.addNewStudyHall(studyHall);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @PutMapping(path = "/{studyHallId}/update")
    public void updateStudyHall(@PathVariable("studyHallId") String studyHallId, @Valid @RequestBody StudyHall studyHall){
        studyHall.setId(studyHallId);
        studyHallRepositoryService.updateStudyHall(studyHall);
    }

    @PreAuthorize("hasRole('ROLE_admin')")
    @DeleteMapping(path = "/{studyHallId}/delete")
    public void deleteStudyHall(@PathVariable("studyHallId") String studyHallId){
        studyHallRepositoryService.deleteStudyHallById(studyHallId);
    }

}
