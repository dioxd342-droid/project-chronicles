package com.projectchronicles.core.combat;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public final class CombatListener implements Listener {
    private final ChroniclesPlugin plugin;
    public CombatListener(ChroniclesPlugin plugin) { this.plugin = plugin; }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        event.setDamage(event.getDamage() * plugin.getClassService().outgoingDamageMultiplier(player));
    }
}
