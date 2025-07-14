package org.rl.shared.model;

/**
 * An enum of possible post states
 */
public enum PostState {
    /**
     * A post that is in the process of being drafted
     */
    DRAFT("DRAFT"),
    /**
     * A fully published post
     */
    PUBLISHED("PUBLISHED");

    private final String state;

    /**
     * An enum containing a string
     * @param state Name of the state
     */
    PostState(String state) {
        this.state = state;
    }

    /**
     * Get the string representation of the string
     * @return Name of the state
     */
    @Override
    public String toString() {
        return this.state;
    }
}
