package org.rl.frontendService.services;

import lombok.AllArgsConstructor;
import org.rl.frontendService.clients.ApiClient;
import org.rl.shared.model.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private ApiClient apiClient;

    public PostResponse getPostById(Integer id) {
        return this.apiClient.getPost(id);
    }
}
