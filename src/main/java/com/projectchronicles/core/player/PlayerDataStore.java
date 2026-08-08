package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public final class PlayerDataStore {
    private final ChroniclesPlugin plugin;
    private final File directory;
    public PlayerDataStore(ChroniclesPlugin plugin) { this.plugin = plugin; this.directory = new File(plugin.getDataFolder(), "players"); }
    public void initialize() { if (!directory.exists() && !directory.mkdirs()) plugin.getLogger().warning("Could not create player data directory: " + directory); }
    public PlayerProfile load(UUID uniqueId) {
        File file = new File(directory, uniqueId + ".yml");
        int level = plugin.getConfig().getInt("profile.starting-level", 1);
        long experience = plugin.getConfig().getLong("profile.starting-experience", 0);
        long balance = plugin.getConfig().getLong("economy.starting-balance", 100);
        if (!file.exists()) return new PlayerProfile(uniqueId, level, experience, balance);
        YamlConfiguration data = YamlConfiguration.loadConfiguration(file);
        PlayerProfile profile = new PlayerProfile(uniqueId, data.getInt("level", level), data.getLong("experience", experience), data.getLong("balance", balance));
        profile.loadCompletedQuests(new HashSet<>(data.getStringList("quests.completed")));
        profile.loadQuestProgress(readQuestProgress(data));
        profile.loadCosmetics(new HashSet<>(data.getStringList("cosmetics.owned")), new HashSet<>(data.getStringList("cosmetics.equipped")));
        return profile;
    }
    private Map<String, Integer> readQuestProgress(YamlConfiguration data) {
        Map<String, Integer> result = new HashMap<>();
        ConfigurationSection section = data.getConfigurationSection("quests.progress");
        if (section != null) for (String key : section.getKeys(false)) result.put(key, section.getInt(key, 0));
        return result;
    }
    public void save(PlayerProfile profile) {
        File file = new File(directory, profile.getUniqueId() + ".yml");
        YamlConfiguration data = new YamlConfiguration();
        data.set("uuid", profile.getUniqueId().toString());
        data.set("level", profile.getLevel());
        data.set("experience", profile.getExperience());
        data.set("balance", profile.getBalance());
        data.set("quests.completed", profile.getCompletedQuests());
        for (Map.Entry<String, Integer> entry : profile.getQuestProgress().entrySet()) data.set("quests.progress." + entry.getKey(), entry.getValue());
        data.set("cosmetics.owned", profile.getOwnedCosmetics());
        data.set("cosmetics.equipped", profile.getEquippedCosmetics());
        try { data.save(file); } catch (IOException exception) { plugin.getLogger().severe("Could not save profile " + profile.getUniqueId() + ": " + exception.getMessage()); }
    }
}
