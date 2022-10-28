package com.charmingplaces;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
//@EnableMongoRepositories("com.example.demo.repository")
public class CharmingPlacesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CharmingPlacesBackendApplication.class, args);
	}

}
