package com.projectchronicles.core.cosmetic;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class CosmeticService {
    private final ChroniclesPlugin plugin;
    private final List<Cosmetic> cosmetics = List.of(
            new Cosmetic("title_chronikler", "Титул Хроникёр", "Золотой титул поддержки проекта.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("title_guardian", "Титул Хранитель", "Бирюзовый титул.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("title_founder", "Титул Основатель", "Особый титул ранних игроков.", Cosmetic.CosmeticType.TITLE),
            new Cosmetic("particle_embers", "Искры", "Косметические искры вокруг игрока.", Cosmetic.CosmeticType.PARTICLE),
            new Cosmetic("particle_stars", "Звёздная пыль", "Мягкий звёздный эффект.", Cosmetic.CosmeticType.PARTICLE),
            new Cosmetic("pet_wisp", "Дух-хранитель", "Косметический спутник игрока.", Cosmetic.CosmeticType.PET)
    );

    public CosmeticService(ChroniclesPlugin plugin) { this.plugin = plugin; }

    public List<Cosmetic> getCosmetics() { return cosmetics; }

    public void showCollection(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Коллекция Chronicles ===");
        for (Cosmetic cosmetic : cosmetics) {
            player.sendMessage(ChatColor.YELLOW + cosmetic.name() + ChatColor.GRAY + " — " + cosmetic.description());
        }
        player.sendMessage(ChatColor.DARK_GRAY + "Косметика не даёт боевого преимущества.");
    }
}
