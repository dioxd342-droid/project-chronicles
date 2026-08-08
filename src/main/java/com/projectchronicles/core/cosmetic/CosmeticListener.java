package com.projectchronicles.core.cosmetic;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class CosmeticListener implements Listener {
    private final ChroniclesPlugin plugin;
    private final CosmeticService service;
    public CosmeticListener(ChroniclesPlugin plugin, CosmeticService service) { this.plugin = plugin; this.service = service; }
    @EventHandler public void onJoin(PlayerJoinEvent event) { service.apply(event.getPlayer()); plugin.getCosmeticPetService().refresh(event.getPlayer()); }
    @EventHandler public void onClick(InventoryClickEvent event) { if (!CosmeticService.GUI_TITLE.equals(event.getView().getTitle())) return; event.setCancelled(true); if (event.getWhoClicked() instanceof org.bukkit.entity.Player player) service.handleClick(player, event.getCurrentItem()); }
    @EventHandler public void onDrag(InventoryDragEvent event) { if (CosmeticService.GUI_TITLE.equals(event.getView().getTitle())) event.setCancelled(true); }
    @EventHandler public void onQuit(PlayerQuitEvent event) { plugin.getCosmeticPetService().remove(event.getPlayer()); }
}
