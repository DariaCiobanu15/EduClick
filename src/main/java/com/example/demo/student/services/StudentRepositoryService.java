package com.example.demo.student.services;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.helperObj.Credentials;
import com.example.demo.student.helperObj.UniversityData;
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

    public Optional<Student> getStudent(String studentId) {
        return repo.findById(studentId);
    }

    public List<Student> findAll() {
        List<Student> student = new ArrayList<Student>();
        Iterator<Student> it = repo.findAll().iterator();
        while(it.hasNext()) {
            student.add(it.next());
        }
        return student;
    }

    public List<Student> findByFirstName(String userName) {
        return repo.findByUserName(userName);
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
        List<Student> existing_users = (List<Student>) repo.findAll();
        List<String> user_names = new ArrayList<String>();
        for(Student s: existing_users){
            user_names.add(s.getUserName());
        }
        if(user_names.contains(student.getUserName())){
            throw new IllegalStateException("username taken");
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
    public void updateStudent(String studentId, String userName, String firstName, String lastName, Integer year, String group, Integer age) {
        Student stud = repo.findById(studentId).orElseThrow( () ->
            new IllegalStateException("Student with id" + studentId + "doesn't exist"));
        if(userName != null) {
            List<Student> existing_users = (List<Student>) repo.findAll();
            List<String> user_names = new ArrayList<String>();
            for(Student s: existing_users){
                user_names.add(s.getUserName());
            }
            if(user_names.contains(userName)) {
                throw new IllegalStateException("username taken");
            }
            stud.setUserName(userName);
        }
        if(age != null) {
            Credentials credentials = stud.getCredentials();
            credentials.setAge(age);
        }
        if(firstName != null) {
            Credentials credentials = stud.getCredentials();
            credentials.setFirstName(firstName);
        }
        if(lastName != null) {
            Credentials credentials = stud.getCredentials();
            credentials.setLastName(lastName);
        }
        if(year != null) {
            UniversityData universityData = stud.getUniversityData();
            universityData.setYear(year);
        }
        if(group != null) {
            UniversityData universityData = stud.getUniversityData();
            universityData.setGroup(group);
        }
    }
}
