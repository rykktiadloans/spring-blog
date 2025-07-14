package org.rl.authService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entrypoint class
 */
@SpringBootApplication
public class AuthServiceApplication {

	/**
	 * Entrypoint method
	 * @param args CLI arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
