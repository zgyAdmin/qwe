package com.turing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.turing.mapper")
public class Purchase2Application {

	public static void main(String[] args) {
		SpringApplication.run(Purchase2Application.class, args);
	}

}
