package com.projectchronicles.core.listener;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;
import com.projectchronicles.core.quest.Quest;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoinListener implements Listener {

    private final ChroniclesPlugin plugin;

    public PlayerJoinListener(ChroniclesPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(event.getPlayer().getUniqueId());
        String welcome = plugin.getConfig().getString("messages.welcome", "&6Добро пожаловать в &eProject Chronicles&6!");

        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', welcome));
        event.getPlayer().sendMessage(ChatColor.GRAY + "Твой уровень: " + ChatColor.YELLOW + profile.getLevel());

        Quest firstSteps = plugin.getQuestService().getQuest("first_steps");
        if (firstSteps != null && plugin.getQuestService().canComplete(firstSteps, event.getPlayer())) {
            plugin.getQuestService().complete(firstSteps, event.getPlayer());
        }
    }
}
