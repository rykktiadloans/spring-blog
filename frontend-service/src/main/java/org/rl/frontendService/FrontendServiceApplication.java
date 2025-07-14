package org.rl.frontendService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entrypoint class
 */
@SpringBootApplication
@EnableFeignClients
public class FrontendServiceApplication {

	/**
	 * Entrypoint method
	 * @param args CLI arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FrontendServiceApplication.class, args);
	}

}
