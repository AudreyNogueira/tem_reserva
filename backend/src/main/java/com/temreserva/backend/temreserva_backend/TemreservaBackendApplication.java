package com.temreserva.backend.temreserva_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TemreservaBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(TemreservaBackendApplication.class, args);
	}
}
