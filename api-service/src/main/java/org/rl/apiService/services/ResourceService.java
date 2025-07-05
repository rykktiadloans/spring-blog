package org.rl.apiService.services;

import org.rl.apiService.configuration.StorageConfiguration;
import org.rl.apiService.model.Resource;
import org.rl.apiService.repositories.ResourceRepository;
import org.rl.shared.exceptions.MissingEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageConfiguration storageConfiguration;

    public Resource getById(int id) {
        Optional<Resource> resource = this.resourceRepository.findById(id);
        if(resource.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Resource entity with ID '%d' cannot be found", id));
        }
        return resource.get();
    }

    public Resource getByName(String name) {
        Optional<Resource> resource = this.resourceRepository.findByName(name);
        if(resource.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Resource entity with name '%s' cannot be found", name));
        }
        return resource.get();
    }

    public File getFileFromResource(Resource resource) {
        return this.storageService.getFile(resource);
    }

    public boolean doesResourceExist(String filename) {
        return this.storageService.getFile(filename).isFile();
    }

    public Path getPath(Resource resource) {
        return Path.of(this.storageConfiguration.getLocation() + "/" + resource.getName());
    }

    public Resource save(Resource resource) {
        return this.resourceRepository.save(resource);
    }

    public Resource save(MultipartFile file) {
        Resource resource = this.storageService.store(file);
        return this.resourceRepository.save(resource);

    }
}
