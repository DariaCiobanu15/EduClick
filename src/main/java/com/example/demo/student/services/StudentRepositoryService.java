package com.example.demo.student.services;

import com.example.demo.student.componentObj.Course;
import com.example.demo.student.componentObj.Student;
import com.example.demo.student.repositories.student.StudentRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Getter
@Setter
@Qualifier("StudentRepositoryService")
public class StudentRepositoryService {

    private List<Course> allCourses;
    @Autowired
    private StudentRepository studentRepository;

    public Optional<Student> getStudent(String studentId) {
        return studentRepository.findById(studentId);
    }

    public List<Student> findAll() {
        List<Student> student = new ArrayList<Student>();
        Iterator<Student> it = studentRepository.findAll().iterator();
        while(it.hasNext()) {
            student.add(it.next());
        }
        return student;
    }

    public void create(Student student) {
        studentRepository.save(student);
    }

    public void update(Student student) {
        studentRepository.save(student);
    }

    public void delete(Student student) {
        studentRepository.delete(student);
    }

    public List<Student> getStudents(){
        return (List<Student>) studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        System.out.println(student);
        List<Student> existing_users = (List<Student>) studentRepository.findAll();
        List<String> user_names = new ArrayList<String>();
        for(Student s: existing_users){
            user_names.add(s.getUsername());
        }
        if(user_names.contains(student.getUsername())) {
            throw new IllegalStateException("username taken");
        }else if(!student.getEmail().contains("@")){
            throw new IllegalStateException("incorrect email");
        } else {
            studentRepository.save(student);
        }
    }
    public void deleteStudent(String studentId){
        boolean exists = studentRepository.existsById(studentId);
        if(!exists) {
            throw new IllegalStateException("Student with id" + studentId + "doesn't exist");
        }
        studentRepository.deleteById(studentId);
    }

}
