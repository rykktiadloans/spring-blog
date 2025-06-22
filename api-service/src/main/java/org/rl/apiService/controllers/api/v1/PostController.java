package org.rl.apiService.controllers.api.v1;

import jakarta.validation.Valid;
import org.rl.apiService.model.Post;
import org.rl.apiService.model.PostState;
import org.rl.apiService.model.api_dto.PostRequest;
import org.rl.apiService.model.api_dto.PostResponse;
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
    PostService postService;
    @GetMapping("")
    List<PostResponse> getPosts(Pageable page) {
        return this.postService.getByPage(page).stream()
                .map(PostResponse::fromPost).toList();
    }

    @GetMapping("/{id}")
    PostResponse getPost(@PathVariable(name = "id") Integer id) {
        return PostResponse.fromPost(this.postService.getById(id));
    }

    @PostMapping("")
    PostResponse postNewPost(@Valid @RequestBody PostRequest postDto) {
        Post post = Post.builder()
                .title(postDto.title())
                .content(postDto.content())
                .creationDate(LocalDateTime.now())
                .state(PostState.DRAFT)
                .build();
        return PostResponse.fromPost(this.postService.save(post));

    }
}
