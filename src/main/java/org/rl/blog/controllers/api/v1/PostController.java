package org.rl.blog.controllers.api.v1;

import org.rl.blog.model.Post;
import org.rl.blog.model.PostState;
import org.rl.blog.model.api_dto.PostDto;
import org.rl.blog.repositories.PostRepository;
import org.rl.blog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.Format;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/posts", produces = "application/json")
public class PostController {
    @Autowired
    PostService postService;
    @GetMapping("")
    List<PostDto> getPosts(Pageable page) {
        return this.postService.getByPage(page).stream()
                .map(PostDto::fromPost).toList();
    }

    @GetMapping("/{id}")
    PostDto getPost(@PathVariable(name = "id") Integer id) {
        return PostDto.fromPost(this.postService.getById(id));
    }

    @Profile("dev")
    @GetMapping("/create")
    PostDto create() {
        Post post = Post.builder()
                .title("Title")
                .content("Blha blah")
                .creationDate(LocalDateTime.from(Instant.now()))
                .state(PostState.PUBLISHED)
                .build();
        return PostDto.fromPost(this.postService.save(post));
    }
}
