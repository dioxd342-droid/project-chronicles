package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public final class PlayerDataStore {

    private final ChroniclesPlugin plugin;
    private final File directory;

    public PlayerDataStore(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.directory = new File(plugin.getDataFolder(), "players");
    }

    public void initialize() {
        if (!directory.exists() && !directory.mkdirs()) {
            plugin.getLogger().warning("Could not create player data directory: " + directory);
        }
    }

    public PlayerProfile load(UUID uniqueId) {
        File file = new File(directory, uniqueId + ".yml");
        int defaultLevel = plugin.getConfig().getInt("profile.starting-level", 1);
        long defaultExperience = plugin.getConfig().getLong("profile.starting-experience", 0);

        if (!file.exists()) {
            return new PlayerProfile(uniqueId, defaultLevel, defaultExperience);
        }

        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        return new PlayerProfile(
                uniqueId,
                data.getInt("level", defaultLevel),
                data.getLong("experience", defaultExperience)
        );
    }

    public void save(PlayerProfile profile) {
        File file = new File(directory, profile.getUniqueId() + ".yml");
        YamlConfiguration data = new YamlConfiguration();
        data.set("uuid", profile.getUniqueId().toString());
        data.set("level", profile.getLevel());
        data.set("experience", profile.getExperience());

        try {
            data.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not save profile " + profile.getUniqueId() + ": " + exception.getMessage());
        }
    }

    public void delete(UUID uniqueId) {
        File file = new File(directory, uniqueId + ".yml");
        if (file.exists() && !file.delete()) {
            plugin.getLogger().warning("Could not delete profile: " + uniqueId);
        }
    }
}
