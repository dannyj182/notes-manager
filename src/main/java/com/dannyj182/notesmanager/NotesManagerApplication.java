package com.dannyj182.notesmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NotesManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotesManagerApplication.class, args);
	}

}
