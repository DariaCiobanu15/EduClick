package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("StudentRepositoryService")
public class StudentRepositoryService {

    @Autowired
    private StudentRepository repo;

    public Optional<Student> findOne(String id) {
        return repo.findById(id);
    }
    public List<Student> findAll() {
        List<Student> student = new ArrayList<Student>();
        Iterator<Student> it = repo.findAll().iterator();
        while(it.hasNext()) {
            student.add(it.next());
        }
        return student;
    }

    public List<Student> findByFirstName(String firstName) {
        return repo.findByName(firstName);
    }

    public void create(Student student) {
        repo.save(student);
    }

    public void update(Student student) {
        repo.save(student);
    }

    public void delete(Student student) {
        repo.delete(student);
    }

    public List<Student> getStudents(){
        return (List<Student>) repo.findAll();
    }

    public void addNewStudent(Student student) {
        System.out.println(student);
        List<Student> named_studs = (List<Student>) repo.findAll();
        List<String> names = new ArrayList<String>();
        for(Student s: named_studs){
            names.add(s.getName());
        }
        if(names.contains(student.getName())){
            throw new IllegalStateException("name taken");
        } else {
            repo.save(student);
        }
    }
    public void deleteStudent(String studentId){
        boolean exists = repo.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("Student with id" + studentId + "doesn't exist");
        }
        repo.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(String studentId, String name, Integer age) {
        Student stud = repo.findById(studentId).orElseThrow( () ->
            new IllegalStateException("Student with id" + studentId + "doesn't exist"));
        if(name != null) {
            List<Student> named_studs = (List<Student>) repo.findAll();
            List<String> names = new ArrayList<String>();
            for(Student s: named_studs){
                names.add(s.getName());
            }
            if(names.contains(name)) {
                throw new IllegalStateException("name taken");
            }
            stud.setName(name);
        }
        if(age != null)
            stud.setAge(age);
    }
}
