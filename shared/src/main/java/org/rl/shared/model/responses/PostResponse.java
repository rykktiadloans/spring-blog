package org.rl.shared.model.responses;

import lombok.experimental.FieldNameConstants;
import org.rl.shared.model.PostState;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A record containing the data about a post
 * @param id ID of the post
 * @param title Title of the post
 * @param content Contents of the post
 * @param creationDate Creation/Publishing date of the post
 * @param state State of the post
 */
@FieldNameConstants
public record PostResponse(Integer id, String title, String content, String summary, LocalDateTime creationDate, PostState state) implements Serializable {

}
