 package com.zff.springboot_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Biz MES Service entry point.
 */
@SpringBootApplication
@EnableFeignClients
public class BizApplication {

	public static void main(String[] args) {
		SpringApplication.run(BizApplication.class, args);
	}

}
