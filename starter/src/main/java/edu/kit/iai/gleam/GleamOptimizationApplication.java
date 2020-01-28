package edu.kit.iai.gleam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication
public class GleamOptimizationApplication {
private static StarterController conn;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(GleamOptimizationApplication.class, args);
		conn = new StarterController();
		
		
	}
}
			
