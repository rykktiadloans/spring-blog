package org.rl.apiService.services;

import org.rl.apiService.model.Resource;
import org.rl.apiService.repositories.ResourceRepository;
import org.rl.shared.exceptions.MissingEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResourceService {
    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private StorageService storageService;

    public Resource getById(int id) {
        Optional<Resource> resource = this.resourceRepository.findById(id);
        if(resource.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Resource entity with ID '%d' cannot be found", id));
        }
        return resource.get();
    }

    public boolean doesResourceExist(String filename) {
        return this.storageService.getFile(filename).isFile();
    }

    public Resource save(Resource resource) {
        return this.resourceRepository.save(resource);
    }
}
