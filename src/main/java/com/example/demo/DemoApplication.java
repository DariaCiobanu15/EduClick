package com.example.demo;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		ApplicationContext appContext =  SpringApplication.run(DemoApplication.class, args);
	    StudentRepository studentRep = (StudentRepository) appContext.getBean("studentRepository");
		studentRep.deleteAll();
		studentRep.save(new Student(null, "Maria", 22));
		studentRep.save(new Student(null, "Ana", 20));
		studentRep.save(new Student(null, "Mihai", 21));
	}


}
