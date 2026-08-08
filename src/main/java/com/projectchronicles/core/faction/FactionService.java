package com.projectchronicles.core.faction;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.entity.Player;

import java.util.List;

public final class FactionService {
    private final ChroniclesPlugin plugin;
    private final List<Faction> factions = List.of(
            new Faction("wardens", "Стражи", "Защищают города, дороги и мирных жителей."),
            new Faction("traders", "Вольные торговцы", "Караваны, рынки и независимые купцы."),
            new Faction("seekers", "Искатели", "Исследуют древние руины и тайны мира.")
    );
    public FactionService(ChroniclesPlugin plugin) { this.plugin = plugin; }
    public List<Faction> getFactions() { return factions; }
    public Faction getFaction(String id) { return factions.stream().filter(f -> f.id().equalsIgnoreCase(id)).findFirst().orElse(null); }
    public int getReputation(Player player, String factionId) { return plugin.getPlayerManager().getProfile(player.getUniqueId()).getReputation(factionId); }
    public void addReputation(Player player, String factionId, int amount) { if (getFaction(factionId) != null) plugin.getPlayerManager().getProfile(player.getUniqueId()).addReputation(factionId, amount); }
    public String getRank(int reputation) {
        if (reputation >= 1000) return "Союзник";
        if (reputation >= 500) return "Уважаемый";
        if (reputation >= 100) return "Дружелюбный";
        if (reputation <= -500) return "Враг";
        if (reputation < 0) return "Недоверие";
        return "Нейтральный";
    }
}
