package org.rl.shared.model.responses;

import java.util.List;

/**
 * Record with data about a resource
 * @param id ID of the post
 * @param name Name of the post
 */
public record ResourceResponse(Integer id, String name) {
}
