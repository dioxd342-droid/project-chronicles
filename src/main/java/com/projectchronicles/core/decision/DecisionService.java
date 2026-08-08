package com.projectchronicles.core.decision;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.faction.FactionService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class DecisionService {
    public static final String ORIGIN_FACTION = "origin_faction";

    private final ChroniclesPlugin plugin;
    private final DecisionStore store;

    public DecisionService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.store = new DecisionStore(plugin);
        store.initialize();
    }

    public String get(Player player, String key) { return store.get(player.getUniqueId(), key); }

    public boolean chooseOriginFaction(Player player, String factionId) {
        if (plugin.getFactionService().getFaction(factionId) == null) return false;
        if (store.get(player.getUniqueId(), ORIGIN_FACTION) != null) return false;
        store.set(player.getUniqueId(), ORIGIN_FACTION, factionId);
        FactionService factions = plugin.getFactionService();
        factions.addReputation(player, factionId, 100);
        if ("wardens".equals(factionId)) factions.addReputation(player, "traders", -50);
        if ("traders".equals(factionId)) factions.addReputation(player, "wardens", -50);
        player.sendMessage(ChatColor.GOLD + "Твой выбор записан в историю Chronicles.");
        return true;
    }
}
