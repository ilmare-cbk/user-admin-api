package com.cbk.user_admin_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class UserAdminApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAdminApiApplication.class, args);
    }

}
