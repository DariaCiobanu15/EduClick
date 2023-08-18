package com.example.demo;
import com.example.demo.student.repositories.student.StudentRepository;
import com.example.demo.student.repositories.course.CourseRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.example.demo.student.*" })
@EnableJpaRepositories(basePackages = {"com.example.demo.student.repositories.course", "com.example.demo.student.repositories.student"})
//@ComponentScan(basePackages = { "com.example.demo.student.repositories.student", "com.example.demo.student.repositories.course" })
public class DemoApplication {

	public static void main(String[] args) {

		ApplicationContext appContext =  SpringApplication.run(DemoApplication.class, args);
	    StudentRepository studentRep = (StudentRepository) appContext.getBean("studentRepository", StudentRepository.class);
		CourseRepository courseRep = (CourseRepository) appContext.getBean("courseRepository", CourseRepository.class);
	}
}
