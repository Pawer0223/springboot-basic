package com.mountain.voucherapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class VoucherAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoucherAppApplication.class, args);
	}

}
