package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.entity.Player;

public final class ExperienceService {

    private final ChroniclesPlugin plugin;

    public ExperienceService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean addExperience(Player player, long amount) {
        if (amount <= 0) {
            return false;
        }

        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        profile.addExperience(amount);
        return true;
    }

    public long experienceRequiredForNextLevel(int level) {
        long base = plugin.getConfig().getLong("progression.base-experience", 100);
        long growth = plugin.getConfig().getLong("progression.experience-growth", 50);
        return base + Math.max(0, level - 1L) * growth;
    }
}
