package org.rl.apiService.services;

import org.rl.apiService.exceptions.MissingEntityException;
import org.rl.apiService.model.Post;
import org.rl.apiService.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    public Post getById(int id) {
        Optional<Post> post = this.postRepository.findById(id);
        if (post.isEmpty()) {
            throw new MissingEntityException(
                    String.format("Post entity with ID '%d' cannot be found", id));
        }
        return post.get();
    }

    public List<Post> getByPage(Pageable pageable) {
        return this.postRepository.findAllByOrderByCreationDateDesc(pageable);
    }

    public Post save(Post post) {
        return this.postRepository.save(post);
    }
}
