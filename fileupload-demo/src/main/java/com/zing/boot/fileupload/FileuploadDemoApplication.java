package com.zing.boot.fileupload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FileuploadDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileuploadDemoApplication.class, args);

	}
}
