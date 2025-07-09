package org.rl.apiService.repositories;

import org.rl.apiService.model.Post;
import org.rl.shared.model.PostState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByOrderByCreationDateDesc(Pageable pageable);

    List<Post> findByStateOrderByCreationDateDesc(PostState state, Pageable pageable);

}
