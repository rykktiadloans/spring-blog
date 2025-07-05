package org.rl.apiService.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties("storage")
@Getter
@Configuration
public class StorageConfiguration {
    private final String location = "uploaded-resources";
}
