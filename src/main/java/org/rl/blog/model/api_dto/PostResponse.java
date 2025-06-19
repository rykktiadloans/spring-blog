package org.rl.blog.model.api_dto;

import org.rl.blog.model.Post;
import org.rl.blog.model.PostState;

import java.time.LocalDateTime;

public record PostResponse(Integer id, String title, String content, LocalDateTime creationDate, PostState state) {

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(), post.getTitle(), post.getContent(),
                post.getCreationDate(), post.getState());
    }
}
