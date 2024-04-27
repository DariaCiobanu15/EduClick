package com.example.demo.student.services.teacher;

import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.services.teacher.TeacherService;
import org.springframework.data.couchbase.core.CouchbaseTemplate;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.couchbase.core.query.QueryCriteria.where;

public class TeacherTemplateService implements TeacherService {
    private static final String DESIGN_DOC = "teacher";
    private CouchbaseTemplate template;

    @Override
    public void create(Teacher teacher) {
        template.insertById(Teacher.class).one(teacher);
    }

    @Override
    public void update(Teacher teacher) {
        template.removeById(Teacher.class).one(teacher.getId());
    }

    @Override
    public void delete(Teacher teacher) {
        template.removeById(Teacher.class).one(teacher.getId());
    }

    @Override
    public Optional<Teacher> findOne(String id) {
        return Optional.of(template.findById(Teacher.class).one(id));
    }

    @Override
    public List<Teacher> findAll() {
        return template.findByQuery(Teacher.class).all();
    }

    @Override
    public Teacher findByUsername(String username) {
        return (Teacher)template.findByQuery(Teacher.class).matching(where("username").is(username));
    }
}
