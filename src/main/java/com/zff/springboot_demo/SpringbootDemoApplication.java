package com.zff.springboot_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 应用入口类，负责启动后端服务。
 */
@SpringBootApplication
public class SpringbootDemoApplication {

	/**
	 * 应用启动入口。
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringbootDemoApplication.class, args);
	}

}
