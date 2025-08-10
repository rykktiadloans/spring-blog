package org.rl.apiService.services;

import lombok.extern.slf4j.Slf4j;
import org.rl.apiService.model.Resource;
import org.rl.apiService.repositories.ResourceRepository;
import org.rl.shared.exceptions.MissingEntityException;
import org.rl.apiService.model.Post;
import org.rl.apiService.repositories.PostRepository;
import org.rl.shared.model.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A service object for working with the posts
 */
@Service
@Slf4j
public class PostService {

    private final Pattern imagePattern = Pattern.compile("!\\[(.*)]\\((.+)\\)");
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ResourceRepository resourceRepository;


    /**
     * Return a post with the specific ID
     * @param id ID of the post
     * @throws MissingEntityException Throws if the post isn't found
     * @return Post
     */
    public Post getById(int id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Post entity with ID '%d' cannot be found", id));
        }
        return post.get();
    }

    /**
     * Return a post with the specific ID that is also published
     * @param id ID of the post
     * @exception MissingEntityException Throws if the post isn't found
     * @return Post
     */
    public Post getByIdWhenPublished(int id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isEmpty() || post.get().getState() != PostState.PUBLISHED) {
            throw new MissingEntityException(
                    String.format("Post entity with ID '%d' cannot be found", id));
        }
        return post.get();
    }

    /**
     * Get a list of posts on a specific page
     * @param pageable A page object
     * @return A list of posts
     */
    public List<Post> getByPage(Pageable pageable) {
        return this.postRepository.findAllByOrderByCreationDateDesc(pageable);
    }


    /**
     * Return a list of posts that have a specific state on a specific page
     * @param state The state which posts should contain
     * @param pageable A page object
     * @return A list of posts
     */
    public List<Post> getByPageWhereState(PostState state, Pageable pageable) {
        return this.postRepository.findByStateOrderByCreationDateDesc(state, pageable);
    }

    /**
     * Save a post to the database
     * @param post the post to save
     * @return The saved post
     */
    public Post save(Post post) {
        Matcher matcher = this.imagePattern.matcher(post.getContent());
        List<String> matches = new ArrayList<>();
        while(matcher.find()) {
            matches.add(matcher.group(2));
        }
        List<String> names = matches.stream()
                .map(path -> {
                    String[] split = path.split("/");
                    return split[split.length - 1];
                }).toList();
        List<Resource> resources = this.resourceRepository.findByNameIn(names)
                .stream().map(resource -> {
                    resource.getPosts().add(post);
                    return resource;
                }).toList();
        post.setResources(new HashSet<>(resources));
        Post newPost = this.postRepository.save(post);
        return newPost;
    }
}
