package com.example.demo;
import com.example.demo.student.componentObj.Booking;
import com.example.demo.student.componentObj.Teacher;
import com.example.demo.student.repositories.booking.BookingRepository;
import com.example.demo.student.repositories.post.PostRepository;
import com.example.demo.student.repositories.student.StudentRepository;
import com.example.demo.student.repositories.course.CourseRepository;
import com.example.demo.student.repositories.studyHall.StudyHallRepository;
import com.example.demo.student.repositories.teacher.TeacherRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({ "com.example.demo.student.*" })
@EnableJpaRepositories(basePackages = {"com.example.demo.student.repositories.course",
										"com.example.demo.student.repositories.student",
										"com.example.demo.student.repositories.teacher",
										"com.example.demo.student.repositories.post",
										"com.example.demo.student.repositories.booking",
										"com.example.demo.student.repositories.studyHall"})

public class DemoApplication {

	public static void main(String[] args) {
		ApplicationContext appContext =  SpringApplication.run(DemoApplication.class, args);
	    StudentRepository studentRep = (StudentRepository) appContext.getBean("studentRepository", StudentRepository.class);
		CourseRepository courseRep = (CourseRepository) appContext.getBean("courseRepository", CourseRepository.class);
		TeacherRepository teacherRepository = (TeacherRepository) appContext.getBean("teacherRepository", TeacherRepository.class);
		PostRepository postRepository = (PostRepository) appContext.getBean("postRepository", PostRepository.class);
		BookingRepository bookingRepository = (BookingRepository) appContext.getBean("bookingRepository", BookingRepository.class);
		StudyHallRepository studyHallRepository = (StudyHallRepository) appContext.getBean("studyHallRepository", StudyHallRepository.class);
	}
}
