package com.example.demo;
import com.example.demo.student.repositories.student.StudentRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.demo.student" })
public class DemoApplication {

	public static void main(String[] args) {

		ApplicationContext appContext =  SpringApplication.run(DemoApplication.class, args);
	    StudentRepository studentRep = (StudentRepository) appContext.getBean("studentRepository", StudentRepository.class);
	}
}
