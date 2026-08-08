package com.projectchronicles.core.decision;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DecisionStore {
    private final ChroniclesPlugin plugin;
    private final java.io.File directory;
    private final Map<UUID, Map<String, String>> cache = new HashMap<>();

    public DecisionStore(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.directory = new java.io.File(plugin.getDataFolder(), "decisions");
    }

    public void initialize() {
        if (!directory.exists() && !directory.mkdirs()) {
            plugin.getLogger().warning("Could not create decisions directory.");
        }
    }

    public String get(UUID playerId, String key) {
        return load(playerId).get(key);
    }

    public boolean has(UUID playerId, String key, String value) {
        return value != null && value.equals(get(playerId, key));
    }

    public void set(UUID playerId, String key, String value) {
        if (key == null || value == null) return;
        Map<String, String> values = load(playerId);
        values.put(key, value);
        save(playerId, values);
    }

    private Map<String, String> load(UUID playerId) {
        if (cache.containsKey(playerId)) return cache.get(playerId);
        Map<String, String> values = new HashMap<>();
        java.io.File file = new java.io.File(directory, playerId + ".yml");
        if (file.exists()) {
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            for (String key : data.getKeys(false)) values.put(key, data.getString(key, ""));
        }
        cache.put(playerId, values);
        return values;
    }

    private void save(UUID playerId, Map<String, String> values) {
        java.io.File file = new java.io.File(directory, playerId + ".yml");
        YamlConfiguration data = new YamlConfiguration();
        values.forEach(data::set);
        try { data.save(file); }
        catch (IOException exception) { plugin.getLogger().warning("Could not save decision state: " + exception.getMessage()); }
    }
}
