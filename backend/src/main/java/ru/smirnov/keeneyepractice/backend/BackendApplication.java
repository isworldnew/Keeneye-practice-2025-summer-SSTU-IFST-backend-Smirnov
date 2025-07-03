package ru.smirnov.keeneyepractice.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.smirnov.keeneyepractice.backend.service.InitializerService;

@SpringBootApplication
public class BackendApplication {

	private static InitializerService initializerService;

	public BackendApplication(InitializerService initializerService) {
		BackendApplication.initializerService = initializerService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
		initializerService.initial();
	}
}