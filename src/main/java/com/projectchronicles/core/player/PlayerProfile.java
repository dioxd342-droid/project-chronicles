package com.projectchronicles.core.player;

import java.util.UUID;

public final class PlayerProfile {

    private final UUID uniqueId;
    private int level;
    private long experience;
    private long balance;

    public PlayerProfile(UUID uniqueId, int level, long experience, long balance) {
        this.uniqueId = uniqueId;
        this.level = Math.max(1, level);
        this.experience = Math.max(0, experience);
        this.balance = Math.max(0, balance);
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

    public long getBalance() {
        return balance;
    }

    public void addExperience(long amount) {
        if (amount > 0) {
            experience += amount;
        }
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    public boolean withdraw(long amount) {
        if (amount <= 0 || balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public void deposit(long amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}
