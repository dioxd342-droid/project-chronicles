package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PlayerManager {

    private final ChroniclesPlugin plugin;
    private final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();
    private final PlayerDataStore dataStore;

    public PlayerManager(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.dataStore = new PlayerDataStore(plugin);
    }

    public void initialize() {
        dataStore.initialize();
        plugin.getLogger().info("PlayerManager initialized.");
    }

    public PlayerProfile getProfile(UUID uniqueId) {
        return profiles.computeIfAbsent(uniqueId, dataStore::load);
    }

    public void saveProfile(UUID uniqueId) {
        PlayerProfile profile = profiles.get(uniqueId);
        if (profile != null) {
            dataStore.save(profile);
        }
    }

    public void saveAll() {
        profiles.values().forEach(dataStore::save);
    }

    public void remove(UUID uniqueId) {
        saveProfile(uniqueId);
        profiles.remove(uniqueId);
    }

    public void shutdown() {
        saveAll();
        profiles.clear();
    }
}
