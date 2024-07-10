package com.practice.project06;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ComponentScan(basePackages = "com.practice.project02.student.StudentController")
@EnableCaching
@EnableAsync
@EnableScheduling
public class Project05Application {

	public static void main(String[] args) {
		SpringApplication.run(Project05Application.class, args);
		System.out.println("Executing code on port 8080....");
	}

}
