package com.projectchronicles.core.economy;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;

import java.util.UUID;

public final class EconomyService {

    private final ChroniclesPlugin plugin;

    public EconomyService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public long getBalance(UUID uniqueId) {
        return plugin.getPlayerManager().getProfile(uniqueId).getBalance();
    }

    public boolean deposit(UUID uniqueId, long amount) {
        if (amount <= 0) return false;
        PlayerProfile profile = plugin.getPlayerManager().getProfile(uniqueId);
        profile.deposit(amount);
        return true;
    }

    public boolean withdraw(UUID uniqueId, long amount) {
        if (amount <= 0) return false;
        return plugin.getPlayerManager().getProfile(uniqueId).withdraw(amount);
    }
}
