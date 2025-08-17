package org.rl.apiService.controllers.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.val;
import org.rl.apiService.model.Post;
import org.rl.apiService.model.Resource;
import org.rl.apiService.services.ResourceService;
import org.rl.shared.model.responses.ResourceResponse;
import org.rl.shared.model.responses.SimplePostResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * A REST controller for the uploaded resources, such as pictures
 */
@RestController
@RequestMapping(path = "/api/v1/resources")
@Validated
@Tag(name = "resource_api_controller", description = "API for accessing resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    /**
     * Fetch the resource by its name
     * @param name The name of the resource, which should be a file.
     * @return The contents resource
     */
    @GetMapping(value = "/{name}")
    @Operation(summary = "Returns the contents of a resource")
    public ResponseEntity<FileSystemResource> getResource(@PathVariable(name = "name") String name) {
        Resource resource = this.resourceService.getByName(name);
        File file = this.resourceService.getFileFromResource(resource);
        val fileRes = new FileSystemResource(file);
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(
                            Files.probeContentType(this.resourceService.getPath(resource))))
                    .body(fileRes);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{name}/posts")
    @Operation(summary = "Get the simplified contents of a resource's posts")
    public List<SimplePostResponse> getPostsOfResource(@PathVariable(name = "name") String name) {
        return this.resourceService.getByName(name)
                .getPosts().stream()
                .map(Post::toSimpleResponse).toList();
    }

    /**
     * Save a new resource to the permanent storage
     * @param file The file to save
     * @return The new resource
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "")
    @Operation(summary = "Create a new resource based on a file. *Requires Authorization*")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResourceResponse postResource(@RequestBody @NotNull MultipartFile file) {
        return this.resourceService.save(file).toResponse();
    }

    /**
     * Get a list of resources using a page
     * @param pageable Pageable object
     * @return List of resource responses
     */
    @PostMapping("/list")
    @Operation(summary = "Get a list of resources")
    @SecurityRequirement(name = "Bearer Authentication")
    public List<ResourceResponse> postGetList(@ParameterObject Pageable pageable) {
        return this.resourceService.getByPage(pageable).stream()
                .map(Resource::toResponse).toList();
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Delete a resource, returns true if successful. *Requires Authorization*")
    @SecurityRequirement(name = "Bearer Authentication")
    public boolean deleteResource(@PathVariable(name = "name") String name) {
        Resource resource = this.resourceService.getByName(name);
        return this.resourceService.delete(resource);
    }
}
