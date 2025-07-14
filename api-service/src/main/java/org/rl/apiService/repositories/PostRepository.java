package org.rl.apiService.repositories;

import org.rl.apiService.model.Post;
import org.rl.shared.model.PostState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A repository used for fetching the posts from the database
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    /**
     * Fetch a list of posts on a specific page, sorted from newest to oldest
     * @param pageable The page object
     * @return List of posts
     */
    List<Post> findAllByOrderByCreationDateDesc(Pageable pageable);

    /**
     * Fetch a list of posts on a specific page with a specific {@link PostState}, sorted from newest to oldest
     * @param state State of post
     * @param pageable The page object
     * @return List of posts
     */
    List<Post> findByStateOrderByCreationDateDesc(PostState state, Pageable pageable);

}
