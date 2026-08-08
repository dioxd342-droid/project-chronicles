package com.projectchronicles.core.cosmetic;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CosmeticPetService {
    private final ChroniclesPlugin plugin;
    private final Map<UUID, UUID> activePets = new ConcurrentHashMap<>();

    public CosmeticPetService(ChroniclesPlugin plugin) { this.plugin = plugin; }

    public void refresh(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile.getEquippedCosmetics().contains("pet_wisp")) spawn(player); else remove(player);
    }

    private void spawn(Player player) {
        UUID existingId = activePets.get(player.getUniqueId());
        if (existingId != null) {
            Entity existing = Bukkit.getEntity(existingId);
            if (existing != null && !existing.isDead()) return;
        }
        Allay pet = player.getWorld().spawn(player.getLocation().add(1.2, 0.8, 1.2), Allay.class);
        pet.customName(Component.text("Дух-хранитель").color(NamedTextColor.LIGHT_PURPLE));
        pet.setCustomNameVisible(false);
        pet.setCanPickupItems(false);
        pet.setInvulnerable(true);
        pet.setCollidable(false);
        activePets.put(player.getUniqueId(), pet.getUniqueId());
    }

    public void remove(Player player) {
        UUID id = activePets.remove(player.getUniqueId());
        if (id == null) return;
        Entity entity = Bukkit.getEntity(id);
        if (entity != null) entity.remove();
    }

    public void tick() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
            if (!profile.getEquippedCosmetics().contains("pet_wisp")) { remove(player); continue; }
            spawn(player);
            UUID id = activePets.get(player.getUniqueId());
            Entity pet = id == null ? null : Bukkit.getEntity(id);
            if (pet == null || pet.isDead()) { activePets.remove(player.getUniqueId()); continue; }
            org.bukkit.Location target = player.getLocation().add(1.2, 0.8, 1.2);
            if (pet.getWorld() != player.getWorld() || pet.getLocation().distanceSquared(player.getLocation()) > 25) {
                pet.teleport(target);
            } else {
                org.bukkit.Location current = pet.getLocation();
                current.multiply(0.65).add(target.multiply(0.35));
                pet.teleport(current);
            }
        }
    }

    public void shutdown() {
        activePets.keySet().stream().map(Bukkit::getPlayer).filter(p -> p != null).forEach(this::remove);
        activePets.clear();
    }
}
