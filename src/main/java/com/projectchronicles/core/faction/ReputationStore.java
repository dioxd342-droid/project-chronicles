package com.projectchronicles.core.faction;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ReputationStore {
    private final ChroniclesPlugin plugin;
    private final File directory;
    private final Map<UUID, Map<String, Integer>> cache = new HashMap<>();

    public ReputationStore(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.directory = new File(plugin.getDataFolder(), "reputation");
    }

    public void initialize() { if (!directory.exists()) directory.mkdirs(); }

    public int get(UUID playerId, String factionId) {
        return load(playerId).getOrDefault(factionId, 0);
    }

    public void add(UUID playerId, String factionId, int amount) {
        if (amount == 0) return;
        Map<String, Integer> values = load(playerId);
        values.merge(factionId, amount, Integer::sum);
        save(playerId, values);
    }

    private Map<String, Integer> load(UUID playerId) {
        if (cache.containsKey(playerId)) return cache.get(playerId);
        Map<String, Integer> values = new HashMap<>();
        File file = new File(directory, playerId + ".yml");
        if (file.exists()) {
            YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
            for (String key : data.getKeys(false)) values.put(key, data.getInt(key));
        }
        cache.put(playerId, values);
        return values;
    }

    private void save(UUID playerId, Map<String, Integer> values) {
        File file = new File(directory, playerId + ".yml");
        YamlConfiguration data = new YamlConfiguration();
        values.forEach(data::set);
        try { data.save(file); }
        catch (IOException e) { plugin.getLogger().warning("Could not save reputation: " + e.getMessage()); }
    }
}
