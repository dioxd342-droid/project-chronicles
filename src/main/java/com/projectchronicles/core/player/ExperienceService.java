package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ExperienceService {

    private final ChroniclesPlugin plugin;

    public ExperienceService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean addExperience(Player player, long amount) {
        if (amount <= 0) return false;

        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        profile.addExperience(amount);

        int levelsGained = 0;
        while (profile.getExperience() >= experienceRequiredForNextLevel(profile.getLevel())) {
            long required = experienceRequiredForNextLevel(profile.getLevel());
            profile.addExperience(-required);
            profile.setLevel(profile.getLevel() + 1);
            levelsGained++;
        }

        if (levelsGained > 0) {
            player.sendMessage(ChatColor.GOLD + "✦ Новый уровень: " + ChatColor.YELLOW + profile.getLevel());
            plugin.getPlayerManager().saveProfile(player.getUniqueId());
        }
        return true;
    }

    public long experienceRequiredForNextLevel(int level) {
        long base = plugin.getConfig().getLong("progression.base-experience", 100);
        long growth = plugin.getConfig().getLong("progression.experience-growth", 50);
        return base + Math.max(0, level - 1L) * growth;
    }
}
