package com.projectchronicles.core.listener;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerActivityListener implements Listener {

    private final ChroniclesPlugin plugin;
    private final Map<UUID, Long> miningCooldown = new HashMap<>();

    public PlayerActivityListener(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        long last = miningCooldown.getOrDefault(player.getUniqueId(), 0L);
        if (now - last < 250L) return;

        miningCooldown.put(player.getUniqueId(), now);
        plugin.getExperienceService().addExperience(player, 2);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer == null) return;
        plugin.getExperienceService().addExperience(killer, 5);
    }
}
