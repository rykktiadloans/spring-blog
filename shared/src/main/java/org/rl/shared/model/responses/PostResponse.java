package org.rl.shared.model.responses;

import org.rl.shared.model.PostState;

import java.time.LocalDateTime;

public record PostResponse(Integer id, String title, String content, LocalDateTime creationDate, PostState state) {

}
