package com.galdovich.java.course;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaCourseApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JavaCourseApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hello world");
	}
}