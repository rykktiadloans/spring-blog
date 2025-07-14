package org.rl.shared.model.responses;

import org.rl.shared.model.PostState;

import java.time.LocalDateTime;

/**
 * A record containing the data about a post
 * @param id ID of the post
 * @param title Title of the post
 * @param content Contents of the post
 * @param creationDate Creation/Publishing date of the post
 * @param state State of the post
 */
public record PostResponse(Integer id, String title, String content, LocalDateTime creationDate, PostState state) {

}
