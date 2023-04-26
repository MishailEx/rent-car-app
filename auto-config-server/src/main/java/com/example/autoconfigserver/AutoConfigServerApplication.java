package com.example.autoconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class AutoConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoConfigServerApplication.class, args);
	}

}
