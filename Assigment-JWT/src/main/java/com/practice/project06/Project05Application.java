package com.practice.project06;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
//@ComponentScan(basePackages = "com.practice.project02.student.StudentController")
@EnableCaching
@EnableAsync
@EnableScheduling
public class Project05Application {

	public static void main(String[] args) {
		SpringApplication.run(Project05Application.class, args);
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		String rawPassword = "admin123";
//		String encodedPassword = "$2a$12$diheMtyeAeA8iBeVx.prq.jeVWuRWrtmlnAwcS7bbLbEJp/EtulPW";  // This should match your stored hash
//
//		boolean matches = encoder.matches(rawPassword, encodedPassword);
//		System.out.println("Password matches: " + matches);  // Should print: true
	}

}
