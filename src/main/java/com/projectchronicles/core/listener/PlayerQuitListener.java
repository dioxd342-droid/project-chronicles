package com.projectchronicles.core.listener;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerQuitListener implements Listener {
    private final ChroniclesPlugin plugin;
    public PlayerQuitListener(ChroniclesPlugin plugin) { this.plugin = plugin; }
    @EventHandler public void onQuit(PlayerQuitEvent event) {
        plugin.getCosmeticPetService().remove(event.getPlayer());
        plugin.getPlayerManager().remove(event.getPlayer().getUniqueId());
    }
}
