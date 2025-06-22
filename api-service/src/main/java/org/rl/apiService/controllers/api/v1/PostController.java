package org.rl.apiService.controllers.api.v1;

import jakarta.validation.Valid;
import org.rl.apiService.model.Post;
import org.rl.shared.model.PostState;
import org.rl.apiService.model.api_dto.requests.PostRequest;
import org.rl.shared.model.responses.PostResponse;
import org.rl.apiService.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/posts", produces = "application/json")
public class PostController {
    @Autowired
    private PostService postService;
    @GetMapping("")
    public List<PostResponse> getPosts(Pageable page) {
        return this.postService.getByPage(page).stream()
                .map(Post::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable(name = "id") Integer id) {
        return this.postService.getById(id).toResponse();
    }

    @PostMapping("")
    public PostResponse postNewPost(@Valid @RequestBody PostRequest postDto) {
        Post post = Post.builder()
                .title(postDto.title())
                .content(postDto.content())
                .creationDate(LocalDateTime.now())
                .state(PostState.DRAFT)
                .build();
        return this.postService.save(post).toResponse();

    }
}
