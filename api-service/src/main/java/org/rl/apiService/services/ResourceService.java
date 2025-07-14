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

/**
 * A service object implementing business login for working with Resources
 */
@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private StorageConfiguration storageConfiguration;

    /**
     * Return a resource with a specific ID
     * @param id ID of the resource
     * @throws MissingEntityException Throws if the resource isn't found
     * @return Resource
     */
    public Resource getById(int id) {
        Optional<Resource> resource = this.resourceRepository.findById(id);
        if(resource.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Resource entity with ID '%d' cannot be found", id));
        }
        return resource.get();
    }

    /**
     * Return a resource with a specific name
     * @param name name of the resource
     * @throws MissingEntityException Throws if the resource isn't found
     * @return Resource
     */
    public Resource getByName(String name) {
        Optional<Resource> resource = this.resourceRepository.findByName(name);
        if(resource.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Resource entity with name '%s' cannot be found", name));
        }
        return resource.get();
    }

    /**
     * Return the file corresponding to the resource
     * @param resource Corresponding resource
     * @return File
     */
    public File getFileFromResource(Resource resource) {
        return this.storageService.getFile(resource);
    }

    /**
     * Check whether the resource exists
     * @param filename Name of the file to check
     * @return True if exists, false otherwise
     */
    public boolean doesResourceExist(String filename) {
        return this.storageService.getFile(filename).isFile();
    }

    /**
     * Return the path to the file, corresponding to the resource
     * @param resource Resource whose path to get
     * @return Corresponding path
     */
    public Path getPath(Resource resource) {
        return Path.of(this.storageConfiguration.getLocation() + "/" + resource.getName());
    }

    /**
     * Save the resource entity
     * @param resource Resource to save
     * @return Updated resource
     */
    public Resource save(Resource resource) {
        return this.resourceRepository.save(resource);
    }

    /**
     * Save the file and create a corresponding resource
     * @param file File to save
     * @return New corresponding resource
     */
    public Resource save(MultipartFile file) {
        Resource resource = this.storageService.store(file);
        return this.resourceRepository.save(resource);

    }
}
