package com.projectchronicles.core.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PlayerProfile {
    private final UUID uniqueId;
    private int level;
    private long experience;
    private long balance;
    private final Set<String> completedQuests = new HashSet<>();
    private final Map<String, Integer> reputation = new HashMap<>();
    private final Set<String> decisions = new HashSet<>();

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
    public Map<String, Integer> getReputation() { return Map.copyOf(reputation); }
    public Set<String> getDecisions() { return Set.copyOf(decisions); }
    public void loadCompletedQuests(Set<String> quests) { completedQuests.clear(); if (quests != null) completedQuests.addAll(quests); }
    public void loadReputation(Map<String, Integer> values) { reputation.clear(); if (values != null) reputation.putAll(values); }
    public void loadDecisions(Set<String> values) { decisions.clear(); if (values != null) decisions.addAll(values); }
    public boolean hasCompletedQuest(String questId) { return completedQuests.contains(questId); }
    public void completeQuest(String questId) { if (questId != null) completedQuests.add(questId); }
    public int getReputation(String factionId) { return reputation.getOrDefault(factionId, 0); }
    public void addReputation(String factionId, int amount) { if (factionId != null && amount != 0) reputation.merge(factionId, amount, Integer::sum); }
    public boolean hasDecision(String id) { return decisions.contains(id); }
    public void recordDecision(String id) { if (id != null) decisions.add(id); }
    public void addExperience(long amount) { if (amount > 0) experience += amount; }
    public boolean removeExperience(long amount) { if (amount <= 0 || experience < amount) return false; experience -= amount; return true; }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    public boolean withdraw(long amount) { if (amount <= 0 || balance < amount) return false; balance -= amount; return true; }
    public void deposit(long amount) { if (amount > 0) balance += amount; }
}
