package org.rl.apiService.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Getter
public class StorageConfiguration {
    private final String location = "uploaded-resources";
}
