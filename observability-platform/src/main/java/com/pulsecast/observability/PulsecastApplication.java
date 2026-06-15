package com.pulsecast.observability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PulsecastApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				PulsecastApplication.class,
				args
		);
	}
}
