package com.projectchronicles.core.loot;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public final class LootListener implements Listener {
    private final ChroniclesPlugin plugin;
    public LootListener(ChroniclesPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        plugin.getLootService().roll(killer, event.getEntityType());
    }
}
