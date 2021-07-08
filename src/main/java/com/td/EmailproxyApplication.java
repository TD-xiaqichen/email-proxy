package com.td;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEmailTools
public class EmailproxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailproxyApplication.class, args);
	}

}
