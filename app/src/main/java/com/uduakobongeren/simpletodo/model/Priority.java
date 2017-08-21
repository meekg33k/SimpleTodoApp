package com.uduakobongeren.simpletodo.model;

/**
 * @author Uduak Obong-Eren
 * @since 8/13/17.
 */
public enum Priority {
    HIGH("HIGH"),
    LOW("LOW"),
    MEDIUM("MEDIUM");

    public String level;

    Priority(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
