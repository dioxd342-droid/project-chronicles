package com.projectchronicles.core.quest;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.entity.Player;

public final class ProgressQuestListener implements Listener {
    private final ProgressQuestService service;
    public ProgressQuestListener(ChroniclesPlugin plugin, ProgressQuestService service) { this.service = service; }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        service.onMine(player, event.getBlock().getType());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player != null) service.onKill(player, event.getEntityType());
    }
}
