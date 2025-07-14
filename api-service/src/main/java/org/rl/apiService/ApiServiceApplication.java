package org.rl.apiService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class with the entrypoint method
 */
@SpringBootApplication
public class ApiServiceApplication {

	/**
	 * Start the Spring application
	 * @param args CLI args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiServiceApplication.class, args);
	}

}
