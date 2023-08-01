package com.example.demo;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import com.example.demo.student.helperObj.Credentials;
import com.example.demo.student.helperObj.UniversityData;
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
//		studentRep.deleteAll();
//		Credentials credentials = new Credentials();
//		credentials.setAge(22);
//		credentials.setFirstName("Maria");
//		credentials.setLastName("Popescu");
//		UniversityData universityData = new UniversityData();
//		universityData.setGroup("C123");
//		universityData.setYear(3);
//		studentRep.save(new Student(null, "maria123", credentials, universityData, null));
//		credentials.setAge(21);
//		credentials.setFirstName("Ana");
//		credentials.setLastName("Ionescu");
//		studentRep.save(new Student(null, "anaaa", credentials, universityData, null));
//		credentials.setAge(21);
//		credentials.setFirstName("Mihai");
//		credentials.setLastName("Eminescu");
//		studentRep.save(new Student(null, "mihai777", credentials, universityData, null));
	}


}
