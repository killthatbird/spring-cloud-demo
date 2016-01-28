package com.springdeveloper.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableTask
public class SparkTaskApp {

	@Bean
	public CommandLineRunner commandLineRunner() {
		return new SparkYarnRunner();
	}

	public static void main(String[] args) {
		SpringApplication.run(SparkTaskApp.class, args);
	}

}
