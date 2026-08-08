package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
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
        int level = plugin.getConfig().getInt("profile.starting-level", 1);
        long experience = plugin.getConfig().getLong("profile.starting-experience", 0);
        long balance = plugin.getConfig().getLong("economy.starting-balance", 100);

        if (!file.exists()) return new PlayerProfile(uniqueId, level, experience, balance);

        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        PlayerProfile profile = new PlayerProfile(
                uniqueId,
                data.getInt("level", level),
                data.getLong("experience", experience),
                data.getLong("balance", balance)
        );
        profile.loadCompletedQuests(new HashSet<>(data.getStringList("quests.completed")));
        return profile;
    }

    public void save(PlayerProfile profile) {
        File file = new File(directory, profile.getUniqueId() + ".yml");
        YamlConfiguration data = new YamlConfiguration();
        data.set("uuid", profile.getUniqueId().toString());
        data.set("level", profile.getLevel());
        data.set("experience", profile.getExperience());
        data.set("balance", profile.getBalance());
        data.set("quests.completed", profile.getCompletedQuests());

        try {
            data.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe("Could not save profile " + profile.getUniqueId() + ": " + exception.getMessage());
        }
    }
}
