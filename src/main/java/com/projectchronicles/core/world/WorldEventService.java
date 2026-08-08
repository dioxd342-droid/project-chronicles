package com.projectchronicles.core.world;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ThreadLocalRandom;

public final class WorldEventService {
    private final ChroniclesPlugin plugin;
    private BukkitTask task;
    private String activeEvent = "calm";

    public WorldEventService(ChroniclesPlugin plugin) { this.plugin = plugin; }

    public void start() {
        if (!plugin.getConfig().getBoolean("world-events.enabled", true)) return;
        task = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 20L * 60L, 20L * 60L * 10L);
        plugin.getLogger().info("World event engine started.");
    }

    private void tick() {
        if (Bukkit.getOnlinePlayers().isEmpty()) return;
        String[] events = {"merchant_caravan", "lost_expedition", "ancient_omen"};
        activeEvent = events[ThreadLocalRandom.current().nextInt(events.length)];
        String message = switch (activeEvent) {
            case "merchant_caravan" -> "§6Мировое событие: §eКараван прибыл в пограничные земли. Торговцы предлагают редкие товары.";
            case "lost_expedition" -> "§6Мировое событие: §eЭкспедиция пропала у древних руин. Кто-то должен её найти.";
            default -> "§5Мировое событие: §dВ небе появился древний знак. Старые NPC начинают менять своё поведение.";
        };
        if (plugin.getConfig().getBoolean("world-events.broadcast", true)) Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public String getActiveEvent() { return activeEvent; }
    public void stop() { if (task != null) task.cancel(); }
}
