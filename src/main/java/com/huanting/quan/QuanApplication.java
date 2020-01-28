package com.huanting.quan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/11
 */
@Configuration
@ComponentScan("com.huanting.quan.aspect")
@EnableAspectJAutoProxy
@EnableJpaAuditing
//@EnableWebMvc
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.huanting.quan.controller"})
//@EnableAdminServer
public class QuanApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuanApplication.class, args);
	}

}