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
            Cosmetic c = cosmetics.get(i); Material material = c.type() == Cosmetic.CosmeticType.TITLE ? Material.NAME_TAG : c.type() == Cosmetic.CosmeticType.PARTICLE ? Material.GLOWSTONE_DUST : Material.AMETHYST_SHARD;
            ItemStack item = new ItemStack(material); ItemMeta meta = item.getItemMeta();
            meta.setDisplayName((profile.ownsCosmetic(c.id()) ? "§a" : "§7§m") + c.name());
            meta.setLore(List.of("§7" + c.description(), "", profile.ownsCosmetic(c.id()) ? (profile.getEquippedCosmetics().contains(c.id()) ? "§eНажми, чтобы снять" : "§aНажми, чтобы надеть") : "§cНе получено"));
            meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "cosmetic"), org.bukkit.persistence.PersistentDataType.STRING, c.id()); item.setItemMeta(meta); inv.setItem(i + 10, item);
        }
        player.openInventory(inv);
    }
    public void handleClick(Player player, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return;
        String id = item.getItemMeta().getPersistentDataContainer().get(new org.bukkit.NamespacedKey(plugin, "cosmetic"), org.bukkit.persistence.PersistentDataType.STRING); if (id == null) return;
        PlayerProfile p = plugin.getPlayerManager().getProfile(player.getUniqueId()); Cosmetic c = cosmetics.stream().filter(x -> x.id().equals(id)).findFirst().orElse(null);
        if (c == null || !p.ownsCosmetic(id)) { player.sendMessage("§cЭта косметика ещё не принадлежит тебе."); return; }
        if (p.getEquippedCosmetics().contains(id)) { p.unequipCosmetic(id); player.sendMessage("§7Косметика снята: §f" + c.name()); }
        else {
            if (c.type() == Cosmetic.CosmeticType.TITLE) p.getEquippedCosmetics().stream().filter(x -> x.startsWith("title_")).toList().forEach(p::unequipCosmetic);
            if (c.type() == Cosmetic.CosmeticType.PARTICLE) p.getEquippedCosmetics().stream().filter(x -> x.startsWith("particle_")).toList().forEach(p::unequipCosmetic);
            p.equipCosmetic(id); player.sendMessage("§aКосметика надета: §f" + c.name());
        }
        apply(player); plugin.getCosmeticPetService().refresh(player); openMenu(player);
    }
    public void grant(Player player, String id) { Cosmetic c = cosmetics.stream().filter(x -> x.id().equals(id)).findFirst().orElse(null); if (c == null) { player.sendMessage("§cНеизвестная косметика: " + id); return; } plugin.getPlayerManager().getProfile(player.getUniqueId()).grantCosmetic(id); player.sendMessage("§aПолучена косметика: §f" + c.name()); }
    public void apply(Player player) {
        PlayerProfile p = plugin.getPlayerManager().getProfile(player.getUniqueId()); String title = p.getEquippedCosmetics().stream().filter(x -> x.startsWith("title_")).findFirst().orElse(null);
        String prefix = title == null ? "" : switch (title) { case "title_chronikler" -> "§6[Хроникёр] §r"; case "title_guardian" -> "§b[Хранитель] §r"; case "title_founder" -> "§d[Основатель] §r"; default -> ""; };
        player.setPlayerListName(prefix + player.getName());
    }
    public void tickEffects() { for (Player p : Bukkit.getOnlinePlayers()) { PlayerProfile profile = plugin.getPlayerManager().getProfile(p.getUniqueId()); if (profile.getEquippedCosmetics().contains("particle_embers")) p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(0,1,0), 3, .25,.35,.25,.01); if (profile.getEquippedCosmetics().contains("particle_stars")) p.getWorld().spawnParticle(Particle.END_ROD, p.getLocation().add(0,1.2,0), 2, .3,.4,.3,.01); } }
}
