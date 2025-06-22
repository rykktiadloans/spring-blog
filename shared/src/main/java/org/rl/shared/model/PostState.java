package org.rl.shared.model;

public enum PostState {
    DRAFT("draft"),
    PUBLISHED("published");

    private final String state;

    PostState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.state;
    }
}
