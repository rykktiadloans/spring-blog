package org.rl.apiService.controllers.api.v1;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.val;
import org.rl.apiService.model.Resource;
import org.rl.apiService.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

@RestController
@RequestMapping(path = "/api/v1/resources")
@Validated
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping(value = "/{name}")
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

    @PostMapping(value = "")
    public Resource postResource(@RequestBody @NotNull MultipartFile file) {
        return this.resourceService.save(file);
    }
}
