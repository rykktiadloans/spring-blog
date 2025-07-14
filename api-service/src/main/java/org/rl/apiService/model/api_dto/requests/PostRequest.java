package org.rl.apiService.model.api_dto.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.rl.shared.model.PostState;

/**
 * A record containing the data that should be present when requesting to create/update a post
 * @param id Id of the post, can be null in case the post is only getting uploaded
 * @param title Title of the post
 * @param content Content of the post
 * @param state State of the post
 */
public record PostRequest(
        @Nullable Integer id,
        @NotBlank String title,
        @NotBlank String content,
        @NotNull PostState state
        ) {
}
