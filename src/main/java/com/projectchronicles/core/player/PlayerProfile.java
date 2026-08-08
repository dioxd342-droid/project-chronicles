package com.projectchronicles.core.player;

import java.util.UUID;

public final class PlayerProfile {

    private final UUID uniqueId;
    private int level;
    private long experience;

    public PlayerProfile(UUID uniqueId, int level, long experience) {
        this.uniqueId = uniqueId;
        this.level = Math.max(1, level);
        this.experience = Math.max(0, experience);
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getLevel() {
        return level;
    }

    public long getExperience() {
        return experience;
    }

    public void addExperience(long amount) {
        if (amount <= 0) {
            return;
        }
        experience += amount;
    }
}
