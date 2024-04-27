package com.example.demo.student.services.studyHall;

import com.example.demo.student.componentObj.StudyHall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;
import java.util.Optional;

public class StudyHallTemplateService implements StudyHallService {

    private static final String DESIGN_DOC = "studyHall";
    private CouchbaseTemplate template;

    @Autowired
    public void setCouchbaseTemplate(CouchbaseTemplate template) {
        this.template = template;
    }

    @Override
    public List<StudyHall> getAllStudyHalls() {
        return template.findByQuery(StudyHall.class).all();
    }

    @Override
    public Optional<StudyHall> getStudyHallById(String id) {
        return Optional.of(template.findById(StudyHall.class).one(id));
    }

    @Override
    public void createStudyHall(StudyHall studyHall) {
        template.insertById(StudyHall.class).one(studyHall);

    }

    @Override
    public void deleteStudyHall(StudyHall studyHall) {
        template.removeById(StudyHall.class).one(studyHall.getId());
    }

    @Override
    public void deleteStudyHallbyId(String id) {
        template.removeById(StudyHall.class).one(id);
    }

    @Override
    public void updateStudyHall(StudyHall studyHall) {
        template.removeById(StudyHall.class).one(studyHall.getId());
    }
}
