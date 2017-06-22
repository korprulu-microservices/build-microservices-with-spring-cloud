package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@EnableZipkinServer
@SpringBootApplication
public class InfraZipkinApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfraZipkinApplication.class, args);
	}
}
