package com.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TransferServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransferServiceApplication.class, args);
	}

}
