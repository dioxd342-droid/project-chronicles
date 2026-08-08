package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerManager {

    private final ChroniclesPlugin plugin;
    private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();

    public PlayerManager(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        plugin.getLogger().info("PlayerManager initialized.");
    }

    public PlayerProfile getProfile(UUID uniqueId) {
        int startingLevel = plugin.getConfig().getInt("profile.starting-level", 1);
        long startingExperience = plugin.getConfig().getLong("profile.starting-experience", 0);
        return profiles.computeIfAbsent(uniqueId,
                id -> new PlayerProfile(id, startingLevel, startingExperience));
    }

    public void remove(UUID uniqueId) {
        profiles.remove(uniqueId);
    }

    public void shutdown() {
        profiles.clear();
    }
}
