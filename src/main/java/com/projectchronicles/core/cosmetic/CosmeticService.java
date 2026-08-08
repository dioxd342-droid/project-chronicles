package com.projectchronicles.core.cosmetic;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public final class CosmeticService {
    public static final String GUI_TITLE = "§6Project Chronicles §8• §fКосметика";
    private final ChroniclesPlugin plugin;
    private final List<Cosmetic> cosmetics = List.of(
            new Cosmetic("title_chronikler", "Хроникёр", "Золотой титул поддержки проекта.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("title_guardian", "Хранитель", "Бирюзовый титул.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("title_founder", "Основатель", "Особый титул ранних игроков.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("particle_embers", "Искры", "Косметические искры вокруг игрока.", Cosmetic.CosmeticType.PARTICLE),
            new Cosmetic("particle_stars", "Звёздная пыль", "Мягкий звёздный эффект.", Cosmetic.CosmeticType.PARTICLE),
            new Cosmetic("pet_wisp", "Дух-хранитель", "Косметический спутник игрока.", Cosmetic.CosmeticType.PET)
    );
    public CosmeticService(ChroniclesPlugin plugin) { this.plugin = plugin; }
    public List<Cosmetic> getCosmetics() { return cosmetics; }

    public void openMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, GUI_TITLE);
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        for (int i = 0; i < cosmetics.size(); i++) {
            Cosmetic cosmetic = cosmetics.get(i);
            Material material = cosmetic.type() == Cosmetic.CosmeticType.TITLE ? Material.NAME_TAG : cosmetic.type() == Cosmetic.CosmeticType.PARTICLE ? Material.GLOWSTONE_DUST : Material.AMETHYST_SHARD;
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName((profile.ownsCosmetic(cosmetic.id()) ? "§a" : "§7§m") + cosmetic.name());
            meta.setLore(List.of(
                    "§7" + cosmetic.description(),
                    "",
                    profile.ownsCosmetic(cosmetic.id()) ? (profile.getEquippedCosmetics().contains(cosmetic.id()) ? "§eНажми, чтобы снять" : "§aНажми, чтобы надеть") : "§cНе получено"
            ));
            meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "cosmetic"), org.bukkit.persistence.PersistentDataType.STRING, cosmetic.id());
            item.setItemMeta(meta);
            inv.setItem(i + 10, item);
        }
        player.openInventory(inv);
    }

    public void handleClick(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;
        String id = item.getItemMeta().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(plugin, "cosmetic"), org.bukkit.persistence.PersistentDataType.STRING);
        if (id == null) return;
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        Cosmetic cosmetic = cosmetics.stream().filter(c -> c.id().equals(id)).findFirst().orElse(null);
        if (cosmetic == null || !profile.ownsCosmetic(id)) {
            player.sendMessage("§cЭта косметика ещё не принадлежит тебе.");
            return;
        }
        if (profile.getEquippedCosmetics().contains(id)) {
            profile.unequipCosmetic(id);
            player.sendMessage("§7Косметика снята: §f" + cosmetic.name());
        } else {
            if (cosmetic.type() == Cosmetic.CosmeticType.TITLE) {
                profile.getEquippedCosmetics().stream().filter(existing -> existing.startsWith("title_")).toList().forEach(profile::unequipCosmetic);
            }
            if (cosmetic.type() == Cosmetic.CosmeticType.PARTICLE) {
                profile.getEquippedCosmetics().stream().filter(existing -> existing.startsWith("particle_")).toList().forEach(profile::unequipCosmetic);
            }
            profile.equipCosmetic(id);
            player.sendMessage("§aКосметика надета: §f" + cosmetic.name());
        }
        apply(player);
        openMenu(player);
    }

    public void grant(Player player, String id) {
        Cosmetic cosmetic = cosmetics.stream().filter(c -> c.id().equals(id)).findFirst().orElse(null);
        if (cosmetic == null) { player.sendMessage("§cНеизвестная косметика: " + id); return; }
        plugin.getPlayerManager().getProfile(player.getUniqueId()).grantCosmetic(id);
        player.sendMessage("§aПолучена косметика: §f" + cosmetic.name());
    }

    public void apply(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        String title = profile.getEquippedCosmetics().stream().filter(id -> id.startsWith("title_")).findFirst().orElse(null);
        String prefix = title == null ? "" : switch (title) {
            case "title_chronikler" -> "§6[Хроникёр] §r";
            case "title_guardian" -> "§b[Хранитель] §r";
            case "title_founder" -> "§d[Основатель] §r";
            default -> "";
        };
        player.setPlayerListName(prefix + player.getName());
    }

    public void tickEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
            if (profile.getEquippedCosmetics().contains("particle_embers")) player.getWorld().spawnParticle(Particle.FLAME, player.getLocation().add(0, 1, 0), 3, .25, .35, .25, .01);
            if (profile.getEquippedCosmetics().contains("particle_stars")) player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1.2, 0), 2, .3, .4, .3, .01);
        }
    }
}
