package org.rl.apiService.model.api_dto.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.rl.shared.model.PostState;

public record PostRequest(
        @Nullable Integer id,
        @NotBlank String title,
        @NotBlank String content,
        @NotNull PostState state
        ) {
}
