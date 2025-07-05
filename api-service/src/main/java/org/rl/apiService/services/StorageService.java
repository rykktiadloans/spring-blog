package org.rl.apiService.services;

import jakarta.annotation.PostConstruct;
import org.rl.apiService.configuration.StorageConfiguration;
import org.rl.apiService.model.Resource;
import org.rl.shared.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class StorageService {
    @Autowired
    private StorageConfiguration storageConfiguration;

    @PostConstruct
    private void init() {
        try {
            Files.createDirectories(Paths.get(this.storageConfiguration.getLocation()));
        } catch (IOException e) {
            throw new StorageException(
                    String.format("Could not create directory at `%s`", this.storageConfiguration.getLocation())
                    , e);
        }
    }

    public Resource store(MultipartFile file) {
        try {
            if(file.isEmpty()) {
                throw new StorageException("File is empty");
            }
            Path dir = Paths.get(this.storageConfiguration.getLocation());
            Path destinationPath = dir.resolve(
                    Paths.get(file.getOriginalFilename()));
            if (!destinationPath.getParent().equals(dir)) {
                throw new StorageException(String.format("Attempted to save a file outside of its current directory: `%s`", destinationPath));
            }
            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
            return Resource.builder().name(file.getOriginalFilename()).build();
        }
        catch (IOException e) {
            throw new StorageException(e);
        }
    }

    public File getFile(String filename) {
        Path dir = Paths.get(this.storageConfiguration.getLocation());
        Path destinationPath = dir.resolve(filename);
        return destinationPath.toFile();
    }

    public File getFile(Resource resource) {
        return getFile(resource.getName());
    }

}
