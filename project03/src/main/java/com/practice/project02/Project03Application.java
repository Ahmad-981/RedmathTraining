package com.practice.project02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.practice.project02.student.StudentController")
public class Project03Application {

	public static void main(String[] args) {
		SpringApplication.run(Project03Application.class, args);
		System.out.println("Executing code on port 8080....");
	}

}
