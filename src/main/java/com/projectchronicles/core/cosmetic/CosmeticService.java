package com.projectchronicles.core.cosmetic;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class CosmeticService {
    private final ChroniclesPlugin plugin;
    private final CosmeticDataStore store;
    private final Map<UUID, Set<String>> owned = new HashMap<>();
    private final Map<UUID, Set<String>> equipped = new HashMap<>();
    private final List<Cosmetic> cosmetics = List.of(
            new Cosmetic("title_chronikler", "Титул Хроникёр", "Золотой титул поддержки проекта.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("title_guardian", "Титул Хранитель", "Бирюзовый титул.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("title_founder", "Титул Основатель", "Особый титул ранних игроков.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("particle_embers", "Искры", "Косметические искры вокруг игрока.", Cosmetic.CosmeticType.PARTICLE),
            new Cosmetic("particle_stars", "Звёздная пыль", "Мягкий звёздный эффект.", Cosmetic.CosmeticType.PARTICLE),
            new Cosmetic("pet_wisp", "Дух-хранитель", "Косметический спутник игрока.", Cosmetic.CosmeticType.PET)
    );

    public CosmeticService(ChroniclesPlugin plugin) { this.plugin = plugin; this.store = new CosmeticDataStore(plugin); }
    public List<Cosmetic> getCosmetics() { return cosmetics; }

    private void load(Player player) {
        UUID uuid = player.getUniqueId();
        if (owned.containsKey(uuid)) return;
        CosmeticDataStore.Data data = store.load(uuid);
        owned.put(uuid, new HashSet<>(data.owned()));
        equipped.put(uuid, new HashSet<>(data.equipped()));
    }
    public boolean grant(Player player, String id) {
        load(player); Cosmetic cosmetic = find(id);
        if (cosmetic == null) return false;
        boolean added = owned.get(player.getUniqueId()).add(id);
        save(player); return added;
    }
    public boolean equip(Player player, String id) {
        load(player); Cosmetic cosmetic = find(id);
        if (cosmetic == null || !owned.get(player.getUniqueId()).contains(id)) return false;
        equipped.get(player.getUniqueId()).add(id); save(player); return true;
    }
    public boolean unequip(Player player, String id) {
        load(player); boolean removed = equipped.get(player.getUniqueId()).remove(id); save(player); return removed;
    }
    public Set<String> getOwned(Player player) { load(player); return Set.copyOf(owned.get(player.getUniqueId())); }
    public Set<String> getEquipped(Player player) { load(player); return Set.copyOf(equipped.get(player.getUniqueId())); }
    public Cosmetic find(String id) { return cosmetics.stream().filter(c -> c.id().equalsIgnoreCase(id)).findFirst().orElse(null); }
    public void save(Player player) { UUID uuid = player.getUniqueId(); store.save(uuid, owned.getOrDefault(uuid, Set.of()), equipped.getOrDefault(uuid, Set.of())); }
    public void unload(Player player) { save(player); UUID uuid = player.getUniqueId(); owned.remove(uuid); equipped.remove(uuid); }

    public void showCollection(Player player) {
        load(player); Set<String> playerOwned = owned.get(player.getUniqueId()); Set<String> playerEquipped = equipped.get(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "=== Коллекция Chronicles ===");
        for (Cosmetic cosmetic : cosmetics) {
            boolean has = playerOwned.contains(cosmetic.id()); boolean active = playerEquipped.contains(cosmetic.id());
            player.sendMessage((has ? ChatColor.GREEN + "✓ " : ChatColor.RED + "✗ ") + cosmetic.name() + (active ? ChatColor.AQUA + " [НАДЕТО]" : ""));
            player.sendMessage(ChatColor.GRAY + "  " + cosmetic.id() + " — " + cosmetic.description());
        }
    }
}
