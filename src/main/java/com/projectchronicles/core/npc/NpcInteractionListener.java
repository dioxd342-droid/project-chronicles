package com.projectchronicles.core.npc;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public final class NpcInteractionListener implements Listener {
    private final ChroniclesPlugin plugin;
    private final NpcService npcService;
    public NpcInteractionListener(ChroniclesPlugin plugin, NpcService npcService) { this.plugin = plugin; this.npcService = npcService; }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager) || !npcService.isStoryteller(villager)) return;
        event.setCancelled(true);
        event.getPlayer().sendMessage(ChatColor.GOLD + "Элиан: " + ChatColor.WHITE + "Хроники снова открываются. Перед тобой два пути.");
        event.getPlayer().sendMessage(ChatColor.YELLOW + "/chronicles choose wardens" + ChatColor.GRAY + " — помочь Стражам защитить город.");
        event.getPlayer().sendMessage(ChatColor.YELLOW + "/chronicles choose traders" + ChatColor.GRAY + " — поддержать Вольных торговцев.");
        event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Твой первый выбор повлияет на отношение фракций к тебе.");
    }
}
