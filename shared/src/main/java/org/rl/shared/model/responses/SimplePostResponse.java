package org.rl.shared.model.responses;

/**
 * A simplified post response without post content
 * @param id ID of the post
 * @param title Title of the post
 */
public record SimplePostResponse(Integer id, String title) {
}
