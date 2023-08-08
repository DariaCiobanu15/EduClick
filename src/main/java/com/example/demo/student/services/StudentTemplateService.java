package com.example.demo.student.services;
import com.example.demo.student.componentObj.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.couchbase.core.query.QueryCriteria.where;

public class StudentTemplateService implements StudentService {

    private static final String DESIGN_DOC = "student";

    private CouchbaseTemplate template;

    @Autowired
    public void setCouchbaseTemplate(CouchbaseTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Student> findOne(String id) {
        return Optional.of(template.findById(Student.class).one(id));
    }

    @Override
    public List<Student> findAll() {
        return template.findByQuery(Student.class).all();
    }

    @Override
    public List<Student> findByName(String name) {
        return template.findByQuery(Student.class).matching(where("name").is(name)).all();
    }

    @Override
    public void create(Student student) {
        template.insertById(Student.class).one(student);
    }

    @Override
    public void update(Student student) {
        template.removeById(Student.class).one(student.getId());
    }

    @Override
    public void delete(Student student) {
        template.removeById(Student.class).one(student.getId());
    }
}