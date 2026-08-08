package com.projectchronicles.core.combat;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public final class ClassService {
    private final ChroniclesPlugin plugin;
    public ClassService(ChroniclesPlugin plugin) { this.plugin = plugin; }

    public boolean choose(Player player, String id) {
        ChroniclesClass chosen;
        try { chosen = ChroniclesClass.valueOf(id.toUpperCase()); } catch (IllegalArgumentException ex) { return false; }
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        profile.setPlayerClass(chosen.name());
        applyStats(player);
        plugin.getPlayerManager().saveProfile(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "✦ Твой класс: " + ChatColor.YELLOW + chosen.displayName());
        player.sendMessage(ChatColor.GRAY + chosen.description());
        return true;
    }

    public void applyStats(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        ChroniclesClass clazz;
        try { clazz = ChroniclesClass.valueOf(profile.getPlayerClass()); } catch (IllegalArgumentException ex) { clazz = ChroniclesClass.SEEKER; }
        var attribute = player.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) attribute.setBaseValue(clazz == ChroniclesClass.WARDEN ? 24.0 : 20.0);
        player.setHealth(Math.min(player.getHealth(), player.getMaxHealth()));
    }

    public double outgoingDamageMultiplier(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        return switch (profile.getPlayerClass()) {
            case "WARDEN" -> 0.90;
            case "HUNTER" -> 1.10;
            default -> 1.00;
        };
    }
}
