package org.rl.apiService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rl.apiService.model.Post;
import org.rl.apiService.repositories.PostRepository;
import org.rl.apiService.utils.Comparators;
import org.rl.apiService.utils.PostgreSqlTestContainer;
import org.rl.shared.exceptions.MissingEntityException;
import org.rl.shared.model.PostState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.rl.apiService.utils.ExamplePosts.*;


@SpringBootTest
@ImportTestcontainers(PostgreSqlTestContainer.class)
@DirtiesContext
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository singleUsePostRepository;

    @BeforeEach
    void setup() {
        this.singleUsePostRepository.deleteAll();
    }

    @Test
    void canSavePostsAtAll() {
        assertThatCode(() -> this.postService.save(buildExamplePost1()))
                .doesNotThrowAnyException();
    }

    @Test
    void canFetchPostById() {
        Post example = buildExamplePost1();
        example = this.postService.save(example);

        Post post = this.postService.getById(example.getId());

        assertThat(post)
                .usingRecursiveComparison()
                .ignoringFields(Post.Fields.id, Post.Fields.resources)
                .withEqualsForFields(Comparators.areEqual(), Post.Fields.creationDate)
                .isEqualTo(example);
    }

    @Test
    void canFetchPostsWithPage() {
        Post post1 = this.postService.save(buildExamplePost1());
        Post post2 = this.postService.save(buildExamplePost2());

        List<Post> posts = this.postService.getByPage(Pageable.ofSize(5));
        assertThat(posts)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Post.Fields.resources)
                .containsExactly(post2, post1);
    }


    @Test
    void canFetchPostsWithPageWithSpecificState() {
        Post post1 = this.postService.save(buildExamplePost1());
        Post post2 = this.postService.save(buildExamplePost2());

        List<Post> posts =
                this.postService.getByPageWhereState(PostState.PUBLISHED,
                        Pageable.ofSize(5));

        assertThat(posts)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Post.Fields.resources)
                .containsExactly(post1);
    }

    @Test
    void throwsWhenGetsByIdWhenPublishedADraftedPost() {
        Post example = this.postService.save(buildExamplePost2());
        assertThatThrownBy(() -> this.postService.getByIdWhenPublished(example.getId()))
                .isInstanceOf(MissingEntityException.class);
    }

    @Test
    void canFetchGetsByIdWhenPublishedAPublishedPost() {
        Post example = this.postService.save(buildExamplePost1());
        Post post = this.postService.getByIdWhenPublished(example.getId());

        assertThat(post)
                .usingRecursiveComparison()
                .ignoringFields(Post.Fields.id, Post.Fields.resources)
                .withEqualsForFields(Comparators.areEqual(), Post.Fields.creationDate)
                .isEqualTo(example);
    }
}
