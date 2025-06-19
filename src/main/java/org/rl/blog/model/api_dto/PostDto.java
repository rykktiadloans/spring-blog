package org.rl.blog.model.api_dto;

import org.rl.blog.model.Post;
import org.rl.blog.model.PostState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public record PostDto(Integer id, String title, String content, LocalDateTime creationDate, PostState state) {

    public static PostDto fromPost(Post post) {
        return new PostDto(
                post.getId(), post.getTitle(), post.getContent(),
                post.getCreationDate(), post.getState());
    }
}
