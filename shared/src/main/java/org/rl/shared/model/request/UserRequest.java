package org.rl.shared.model.request;

/**
 * User credentials record
 * @param username Username
 * @param password Password of the user
 */
public record UserRequest(String username, String password) {
}
