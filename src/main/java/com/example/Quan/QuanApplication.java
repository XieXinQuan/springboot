package com.example.Quan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

//@Configuration
//@ComponentScan("com.example.Quan.aspect")
//@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class QuanApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanApplication.class, args);
	}

}
