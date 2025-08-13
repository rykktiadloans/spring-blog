package org.rl.apiService.utils;

import org.rl.apiService.model.Post;
import org.rl.shared.model.PostState;

import java.time.LocalDateTime;
import java.util.HashSet;

public class ExamplePosts {

    public static Post buildExamplePost1() {
        return Post.builder()
                .title("Example title")
                .content("Blah blaaaah blaaaaah")
                .summary("summary")
                .creationDate(LocalDateTime.of(2025, 1, 1, 12, 0, 5))
                .state(PostState.PUBLISHED)
                .resources(new HashSet<>())
                .build();
    }
    public static Post buildExamplePost2() {
        return Post.builder()
                .title("Another title")
                .content("Blah blaaaah blaaaaah")
                .summary("summary")
                .creationDate(LocalDateTime.of(2025, 1, 2, 12, 0, 5))
                .state(PostState.DRAFT)
                .resources(new HashSet<>())
                .build();
    }
}
