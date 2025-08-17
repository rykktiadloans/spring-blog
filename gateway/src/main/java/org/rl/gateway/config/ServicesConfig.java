package org.rl.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ServicesConfig {
    @Value("${services.api}")
    private String apiUrl;
    @Value("${services.frontend}")
    private String frontendUrl;
    @Value("${services.auth}")
    private String authUrl;
}
