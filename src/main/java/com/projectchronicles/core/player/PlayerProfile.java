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
    private final Map<String, Integer> questProgress = new HashMap<>();
    private final Set<String> ownedCosmetics = new HashSet<>();
    private final Set<String> equippedCosmetics = new HashSet<>();

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
    public Map<String, Integer> getQuestProgress() { return Map.copyOf(questProgress); }
    public int getQuestProgress(String id) { return questProgress.getOrDefault(id, 0); }
    public Set<String> getOwnedCosmetics() { return Set.copyOf(ownedCosmetics); }
    public Set<String> getEquippedCosmetics() { return Set.copyOf(equippedCosmetics); }
    public void loadCompletedQuests(Set<String> quests) { completedQuests.clear(); if (quests != null) completedQuests.addAll(quests); }
    public void loadQuestProgress(Map<String, Integer> progress) { questProgress.clear(); if (progress != null) questProgress.putAll(progress); }
    public void setQuestProgress(String id, int value) { if (id != null) questProgress.put(id, Math.max(0, value)); }
    public void loadCosmetics(Set<String> owned, Set<String> equipped) {
        ownedCosmetics.clear(); equippedCosmetics.clear();
        if (owned != null) ownedCosmetics.addAll(owned);
        if (equipped != null) equippedCosmetics.addAll(equipped);
        equippedCosmetics.retainAll(ownedCosmetics);
    }
    public boolean hasCompletedQuest(String id) { return completedQuests.contains(id); }
    public void completeQuest(String id) { if (id != null) completedQuests.add(id); }
    public boolean ownsCosmetic(String id) { return ownedCosmetics.contains(id); }
    public void grantCosmetic(String id) { if (id != null) ownedCosmetics.add(id); }
    public boolean equipCosmetic(String id) { if (!ownedCosmetics.contains(id)) return false; equippedCosmetics.add(id); return true; }
    public void unequipCosmetic(String id) { equippedCosmetics.remove(id); }
    public void clearEquippedCosmetics() { equippedCosmetics.clear(); }
    public void addExperience(long amount) { if (amount > 0) experience += amount; }
    public boolean removeExperience(long amount) { if (amount <= 0 || experience < amount) return false; experience -= amount; return true; }
    public void setLevel(int level) { this.level = Math.max(1, level); }
    public boolean withdraw(long amount) { if (amount <= 0 || balance < amount) return false; balance -= amount; return true; }
    public void deposit(long amount) { if (amount > 0) balance += amount; }
}
