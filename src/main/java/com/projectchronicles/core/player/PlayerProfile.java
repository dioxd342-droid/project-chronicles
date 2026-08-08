package com.projectchronicles.core.player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class PlayerProfile {

    private final UUID uniqueId;
    private int level;
    private long experience;
    private long balance;
    private final Set<String> completedQuests = new HashSet<>();

    public PlayerProfile(UUID uniqueId, int level, long experience, long balance) {
        this.uniqueId = uniqueId;
        this.level = Math.max(1, level);
        this.experience = Math.max(0, experience);
        this.balance = Math.max(0, balance);
    }

    public UUID getUniqueId() { return uniqueId; }
    public int getLevel() { return level; }
    public long getExperience() { return experience; }
    public long getBalance() { return balance; }
    public Set<String> getCompletedQuests() { return Set.copyOf(completedQuests); }

    public void loadCompletedQuests(Set<String> quests) {
        completedQuests.clear();
        if (quests != null) completedQuests.addAll(quests);
    }

    public boolean hasCompletedQuest(String questId) { return completedQuests.contains(questId); }
    public void completeQuest(String questId) { if (questId != null) completedQuests.add(questId); }

    public void addExperience(long amount) { if (amount > 0) experience += amount; }
    public boolean removeExperience(long amount) {
        if (amount <= 0 || experience < amount) return false;
        experience -= amount;
        return true;
    }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    public boolean withdraw(long amount) {
        if (amount <= 0 || balance < amount) return false;
        balance -= amount;
        return true;
    }
    public void deposit(long amount) { if (amount > 0) balance += amount; }
}
