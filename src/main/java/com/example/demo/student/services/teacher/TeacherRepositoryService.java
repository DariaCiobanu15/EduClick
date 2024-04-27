package com.example.demo.student.services.teacher;

import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.repositories.teacher.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("TeacherRepositoryService")
public class TeacherRepositoryService {
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherRepositoryService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }
    public Optional<Teacher> getTeacher(String id) {
        return teacherRepository.findById(id);
    }
    public void create(Teacher teacher) {
        teacherRepository.save(teacher);
    }
    public void update(Teacher teacher) {teacherRepository.save(teacher); }
    public void delete(Teacher teacher) {
        teacherRepository.delete(teacher);
    }
    public List<Teacher> getTeachers() {
        return (List<Teacher>) teacherRepository.findAll();
    }
    public void addNewTeacher(Teacher teacher) {
        List<Teacher> existing_teachers = (List<Teacher>) teacherRepository.findAll();
        List<String> teacher_usernames = new ArrayList<String>();
        for(Teacher t: existing_teachers){
            teacher_usernames.add(t.getUsername());
        }
        if(teacher_usernames.contains(teacher.getUsername())) {
            throw new IllegalStateException("username taken");
        } else {
            teacherRepository.save(teacher);
        }
    }
    public void deleteCourse(String id) {
        boolean exists = teacherRepository.existsById(id);
        if(!exists) {
            throw new IllegalStateException("teacher doesn't exist!");
        }
        teacherRepository.deleteById(id);
    }
}
