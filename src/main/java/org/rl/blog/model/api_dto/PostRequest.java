package org.rl.blog.model.api_dto;

import jakarta.validation.constraints.NotBlank;

public record PostRequest(
        @NotBlank String title,
        @NotBlank String content
) {
}
