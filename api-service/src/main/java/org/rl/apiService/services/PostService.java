package org.rl.apiService.services;

import org.rl.shared.exceptions.MissingEntityException;
import org.rl.apiService.model.Post;
import org.rl.apiService.repositories.PostRepository;
import org.rl.shared.model.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service object for working with the posts
 */
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

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
        return this.postRepository.save(post);
    }
}
