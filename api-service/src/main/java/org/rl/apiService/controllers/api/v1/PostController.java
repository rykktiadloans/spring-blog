package org.rl.apiService.controllers.api.v1;

import jakarta.validation.Valid;
import org.hibernate.annotations.Cache;
import org.rl.apiService.model.Post;
import org.rl.shared.exceptions.MissingEntityException;
import org.rl.shared.model.PostState;
import org.rl.apiService.model.api_dto.requests.PostRequest;
import org.rl.shared.model.responses.PostResponse;
import org.rl.apiService.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;


/**
 * A REST controller for working with {@link Post} entities
 */
@RestController
@RequestMapping(path = "/api/v1/posts", produces = "application/json")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     * Returns {@link PostResponse} entities that are available for all users ({@link PostState} Published)
     * @param page The size and offset of the query
     * @return List of {@link PostResponse} that are published
     */
    @Cacheable(value = "postsCache")
    @GetMapping("")
    public List<PostResponse> getPosts(Pageable page) {
        return this.postService.getByPageWhereState(PostState.PUBLISHED, page).stream()
                .map(Post::toResponse).toList();
    }

    /**
     * Returns {@link PostResponse} entities. Should only available for authorized users
     * @param page The size and offset of the query
     * @return List of {@link PostResponse}
     */
    @Cacheable(value = "anyPostsCache")
    @PostMapping(value = "/anystate")
    public List<PostResponse> getPostsWithState(Pageable page) {
        return this.postService.getByPage(page).stream()
                .map(Post::toResponse).toList();
    }

    /**
     * Returns a specific {@link PostResponse} by its ID. Can only return published posts.
     * @param id ID of the post
     * @return Post response
     */
    @Cacheable(value = "postCache", key = "#id")
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable(name = "id") Integer id) {
        return this.postService.getByIdWhenPublished(id).toResponse();
    }

    /**
     * Returns a specific {@link PostResponse} by its ID. Should be available only for authorized users
     * @param id ID of the post
     * @return Post response
     */
    @Cacheable(value = "postCache", key = "#id")
    @PostMapping("/anystate/{id}")
    public PostResponse getPostAnyState(@PathVariable(name = "id") Integer id) {
        return this.postService.getById(id).toResponse();
    }

    /**
     * Create a new {@link Post}
     * @param postDto A valid Post DTO
     * @return The new post
     */
    @Caching(evict = {
            @CacheEvict(value = "postsCache", allEntries = true),
            @CacheEvict(value = "anyPostsCache", allEntries = true)
    })
    @PostMapping("")
    public PostResponse postNewPost(@Valid @RequestBody PostRequest postDto) {
        Post post = Post.builder()
                .title(postDto.title())
                .content(postDto.content())
                .creationDate(LocalDateTime.now())
                .state(postDto.state())
                .build();
        return this.postService.save(post).toResponse();
    }

    /**
     * Update an old post. If the post's state is transfered from Draft to Published, it also updates the creation date
     * @param postDto The details to be updated
     * @return The updated post
     */
    @Caching(evict = {
            @CacheEvict(value = "postCache", key = "#postDto.id"),
            @CacheEvict(value = "postsCache", allEntries = true),
            @CacheEvict(value = "postsCache", allEntries = true),
            @CacheEvict(value = "anyPostsCache", allEntries = true)
    })
    @PutMapping("")
    public PostResponse putNewPost(@Valid @RequestBody PostRequest postDto) {
        if(postDto.id() == null) {
            throw new MissingEntityException("Post ID is null");
        }
        Post post = this.postService.getById(postDto.id());
        post.setTitle(postDto.title());
        post.setContent(postDto.content());
        post.setState(postDto.state());
        if(postDto.state() == PostState.PUBLISHED && post.getState() == PostState.DRAFT) {
            post.setCreationDate(LocalDateTime.now());
        }
        return this.postService.save(post).toResponse();
    }
}
