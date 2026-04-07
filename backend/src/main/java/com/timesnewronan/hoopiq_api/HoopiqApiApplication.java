package com.timesnewronan.hoopiq_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Interacts directly with the database, contains our logic to save ,delete or find data (CRUD) and hides the complexity of SQL or datbase connections
@SpringBootApplication
public class HoopiqApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoopiqApiApplication.class, args);
	}

	// Add an ew search method

}
