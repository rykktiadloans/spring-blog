package org.rl.apiService.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rl.apiService.model.Post;
import org.rl.apiService.utils.Comparators;
import org.rl.apiService.utils.PostgreSqlTestContainer;
import org.rl.shared.model.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.rl.apiService.utils.ExamplePosts.buildExamplePost1;
import static org.rl.apiService.utils.ExamplePosts.buildExamplePost2;

@SpringBootTest
@ImportTestcontainers(PostgreSqlTestContainer.class)
@DirtiesContext
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setup() {
        this.postRepository.deleteAll();
    }

    @Test
    void canSavePostsAtAll() {
        assertThatCode(() -> this.postRepository.save(buildExamplePost1()))
                .doesNotThrowAnyException();
    }

    @Test
    void canFetchPostById() {
        Post example = buildExamplePost1();
        this.postRepository.save(example);

        Optional<Post> post = this.postRepository.findById(1);

        assertThat(post.isPresent())
                .withFailMessage(() -> "Post could not be found!")
                .isTrue();

        assertThat(post.get())
                .usingRecursiveComparison()
                .ignoringFields(Post.Fields.id)
                .withEqualsForFields(Comparators.areEqual(), Post.Fields.creationDate)
                .isEqualTo(example);
    }

    @Test
    void canFindPostsByOrder() {
        Post post1 = this.postRepository.save(buildExamplePost1());
        Post post2 = this.postRepository.save(buildExamplePost2());

        List<Post> posts = this.postRepository.findAllByOrderByCreationDateDesc(Pageable.ofSize(5));

        assertThat(posts)
                .usingRecursiveFieldByFieldElementComparatorOnFields()
                .containsExactly(post2, post1);
    }

    @Test
    void canFindPostsByOrderFilteredByState() {
        Post post1 = this.postRepository.save(buildExamplePost1());
        Post post2 = this.postRepository.save(buildExamplePost2());

        List<Post> posts =
                this.postRepository.findByStateOrderByCreationDateDesc(PostState.PUBLISHED,
                        Pageable.ofSize(5));

        assertThat(posts)
                .usingRecursiveFieldByFieldElementComparatorOnFields()
                .containsExactly(post1);
    }

}
