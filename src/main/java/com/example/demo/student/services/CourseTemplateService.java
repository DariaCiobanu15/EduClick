package com.example.demo.student.services;
import com.example.demo.student.componentObj.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.couchbase.core.query.QueryCriteria.where;

public class CourseTemplateService implements CourseService {

    private static final String DESIGN_DOC = "course";

    private CouchbaseTemplate template;

    @Override
    public void create(Course course) {
        template.insertById(Course.class).one(course);
    }

    @Override
    public void update(Course course) {
        template.removeById(Course.class).one(course.getId());
    }

    @Override
    public void delete(Course course) {
        template.removeById(Course.class).one(course.getId());
    }

    @Autowired
    public void setCouchbaseTemplate(CouchbaseTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Course> findOne(String id) {
        return Optional.of(template.findById(Course.class).one(id));
    }

    @Override
    public List<Course> findAll() {
        return template.findByQuery(Course.class).all();
    }

    @Override
    public List<Course> findByName(String name) {
        return template.findByQuery(Course.class).matching(where("name").is(name)).all();
    }


}