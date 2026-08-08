package com.projectchronicles.core.economy;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.entity.Player;

import java.util.UUID;

public final class EconomyService {

    private final ChroniclesPlugin plugin;

    public EconomyService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public long getBalance(UUID uniqueId) {
        return plugin.getConfig().getLong("economy.starting-balance", 100);
    }

    public long getBalance(Player player) {
        return getBalance(player.getUniqueId());
    }
}
