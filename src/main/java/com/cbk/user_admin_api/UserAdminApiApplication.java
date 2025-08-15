package com.cbk.user_admin_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class UserAdminApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAdminApiApplication.class, args);
	}

}
