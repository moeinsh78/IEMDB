package com.ie.CA8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@ServletComponentScan
@CrossOrigin(origins = "http://localhost:3000")
@SpringBootApplication
public class Ca8Application {
	public static void main(String[] args) {
		SpringApplication.run(Ca8Application.class, args);
	}

}
