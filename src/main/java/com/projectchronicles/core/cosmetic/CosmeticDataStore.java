package com.projectchronicles.core.cosmetic;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class CosmeticDataStore {
    private final ChroniclesPlugin plugin;
    private final FileDirectory directory;

    public CosmeticDataStore(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.directory = new FileDirectory(plugin);
    }

    public Data load(UUID uuid) {
        YamlConfiguration data = directory.load(uuid);
        return new Data(new HashSet<>(data.getStringList("owned")), new HashSet<>(data.getStringList("equipped")));
    }

    public void save(UUID uuid, Set<String> owned, Set<String> equipped) {
        YamlConfiguration data = new YamlConfiguration();
        data.set("owned", owned);
        data.set("equipped", equipped);
        try { data.save(directory.file(uuid)); }
        catch (IOException e) { plugin.getLogger().warning("Could not save cosmetics for " + uuid + ": " + e.getMessage()); }
    }

    public record Data(Set<String> owned, Set<String> equipped) {}

    private static final class FileDirectory {
        private final java.io.File dir;
        FileDirectory(ChroniclesPlugin plugin) { dir = new java.io.File(plugin.getDataFolder(), "cosmetics"); if (!dir.exists()) dir.mkdirs(); }
        java.io.File file(UUID uuid) { return new java.io.File(dir, uuid + ".yml"); }
        YamlConfiguration load(UUID uuid) { return YamlConfiguration.loadConfiguration(file(uuid)); }
    }
}
