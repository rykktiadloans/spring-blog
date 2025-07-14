package org.rl.apiService.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Configuration of the file storage, such as uploaded pictures
 */
@ConfigurationProperties("storage")
@Getter
@Configuration
public class StorageConfiguration {
    /**
     * Name of the directory where to store uploaded files
     */
    private final String location = "uploaded-resources";
}
