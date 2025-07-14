package org.rl.apiService.model;

/**
 * Possible roles of a user
 */
public enum UserRole {
    /**
     * A role for usual users
     * @deprecated Has no purpose yet
     */
    USER("user"),
    /**
     * A role for the owner of the blog
     */
    OWNER("owner");

    private final String user;

    /**
     * An enum contains a string
     * @param user Name of the role
     */
    UserRole(String user) {
        this.user = user;
    }

    /**
     * Get the string with the name of the role
     * @return Name of the role
     */
    @Override
    public String toString() {
        return this.user;
    }
}
