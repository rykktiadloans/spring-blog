package org.rl.apiService.model;

public enum UserRole {
    USER("user"),
    OWNER("owner");

    private final String user;

    UserRole(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.user;
    }
}
