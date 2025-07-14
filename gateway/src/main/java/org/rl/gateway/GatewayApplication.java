package org.rl.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.WebFilter;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 * Entrypoint class
 */
@SpringBootApplication
@EnableWebFlux
@EnableReactiveFeignClients
public class GatewayApplication {

	/**
	 * Entrypoint method
	 * @param args CLI arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

}
