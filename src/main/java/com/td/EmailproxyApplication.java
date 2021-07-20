package com.td;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableEmailTools
@EnableJms //启动消息队列
public class EmailproxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailproxyApplication.class, args);
	}

}
