package com.projectchronicles.core.player;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class AbilityService {
    private final ChroniclesPlugin plugin;

    public AbilityService(ChroniclesPlugin plugin) { this.plugin = plugin; }

    public boolean use(Player player, String ability) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        String id = ability.toLowerCase();
        int level = profile.getLevel();
        switch (id) {
            case "sprint" -> {
                if (level < 3) return fail(player, "Спринт открывается с 3 уровня.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 8, 1, false, false, true));
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 18, .3, .1, .3, .03);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_STRONG, 1f, 1.4f);
            }
            case "guardian" -> {
                if (level < 5) return fail(player, "Страж открывается с 5 уровня.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 6, 1, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 4, 0, false, false, true));
                player.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, player.getLocation().add(0, 1, 0), 24, .5, .8, .5, .1);
            }
            case "focus" -> {
                if (level < 8) return fail(player, "Фокус открывается с 8 уровня.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 20 * 5, 1, false, false, true));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20 * 8, 1, false, false, true));
                player.getWorld().spawnParticle(Particle.ENCHANT, player.getLocation().add(0, 1, 0), 30, .6, .7, .6, .5);
            }
            default -> { return fail(player, "Неизвестная способность. Доступно: sprint, guardian, focus"); }
        }
        player.sendMessage(ChatColor.LIGHT_PURPLE + "✦ Способность активирована: " + ChatColor.YELLOW + id);
        return true;
    }

    private boolean fail(Player player, String message) {
        player.sendMessage(ChatColor.RED + message);
        return false;
    }
}
