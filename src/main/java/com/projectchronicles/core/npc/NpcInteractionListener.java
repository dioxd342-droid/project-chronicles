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
        String choice = plugin.getDecisionService().get(event.getPlayer(), "origin_faction");
        if (choice != null) {
            event.getPlayer().sendMessage(ChatColor.GOLD + "Элиан: " + ChatColor.WHITE + "Я помню твой путь. Хроники уже меняются вокруг тебя.");
            event.getPlayer().sendMessage(ChatColor.GRAY + "Твой путь: " + ChatColor.YELLOW + choice);
            event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "/chronicles quests — посмотреть доступные задания.");
            return;
        }
        event.getPlayer().sendMessage(ChatColor.GOLD + "Элиан: " + ChatColor.WHITE + "Хроники снова открываются. Перед тобой три пути.");
        event.getPlayer().sendMessage(ChatColor.YELLOW + "/chronicles choose wardens" + ChatColor.GRAY + " — Стражи: порядок и защита городов.");
        event.getPlayer().sendMessage(ChatColor.YELLOW + "/chronicles choose traders" + ChatColor.GRAY + " — Торговцы: свобода, караваны и богатство.");
        event.getPlayer().sendMessage(ChatColor.YELLOW + "/chronicles choose seekers" + ChatColor.GRAY + " — Искатели: руины, тайны и древние знания.");
        event.getPlayer().sendMessage(ChatColor.DARK_GRAY + "Первый выбор повлияет на дальнейшие квесты и репутацию.");
    }
}
