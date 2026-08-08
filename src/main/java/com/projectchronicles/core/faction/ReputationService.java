package com.projectchronicles.core.faction;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ReputationService {

    private final ChroniclesPlugin plugin;
    private final List<Faction> factions = List.of(
            new Faction("wardens", "Стражи", "Защитники городов и дорог."),
            new Faction("free_traders", "Вольные торговцы", "Купцы, караванщики и независимые предприниматели."),
            new Faction("seekers", "Искатели", "Исследователи древних мест и тайн мира.")
    );

    public ReputationService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public int getReputation(UUID playerId, String factionId) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(playerId);
        return profile.getReputation(factionId);
    }

    public void addReputation(UUID playerId, String factionId, int amount) {
        if (amount == 0) return;
        PlayerProfile profile = plugin.getPlayerManager().getProfile(playerId);
        profile.addReputation(factionId, amount);
    }

    public String getRank(int reputation) {
        if (reputation >= 1000) return "Союзник";
        if (reputation >= 500) return "Уважаемый";
        if (reputation >= 100) return "Дружелюбный";
        if (reputation <= -500) return "Враг";
        if (reputation < 0) return "Недоверие";
        return "Нейтральный";
    }
}
