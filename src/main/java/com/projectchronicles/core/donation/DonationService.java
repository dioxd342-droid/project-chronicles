package com.projectchronicles.core.donation;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class DonationService {

    public record Product(String id, String name, String description, int priceRubles, String command) {}

    private final ChroniclesPlugin plugin;
    private final List<Product> products = List.of(
            new Product("chronikler", "Хроникёр", "Уникальный титул и косметический префикс.", 299, "lp user %player% meta setprefix 10 \"&6[Хроникёр] &r\""),
            new Product("guardian", "Хранитель", "Расширенный набор косметических эффектов и титулов.", 599, "lp user %player% meta setprefix 20 \"&b[Хранитель] &r\""),
            new Product("founder", "Основатель", "Эксклюзивный статус основателя проекта.", 1199, "lp user %player% meta setprefix 30 \"&d[Основатель] &r\""),
            new Product("legend", "Легенда Chronicles", "Максимальный косметический набор и особый титул.", 2499, "lp user %player% meta setprefix 40 \"&5[Легенда] &r\"")
    );

    public DonationService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void showStore(Player player) {
        String url = plugin.getConfig().getString("donation.store-url", "");
        player.sendMessage(ChatColor.GOLD + "=== Магазин Project Chronicles ===");
        player.sendMessage(ChatColor.GRAY + "Поддержка проекта • косметика • статус");
        for (Product product : products) {
            player.sendMessage(ChatColor.YELLOW + product.name() + ChatColor.WHITE + " — " + product.priceRubles() + " ₽");
            player.sendMessage(ChatColor.GRAY + "  " + product.description());
        }
        if (url.isBlank() || url.contains("YOUR-")) {
            player.sendMessage(ChatColor.RED + "Магазин ещё не подключён.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Магазин: " + ChatColor.WHITE + url);
        }
    }

    public void showDonationInfo(Player player) {
        player.sendMessage(ChatColor.GOLD + "Донат Chronicles");
        player.sendMessage(ChatColor.GRAY + "299 ₽ → Хроникёр");
        player.sendMessage(ChatColor.GRAY + "599 ₽ → Хранитель");
        player.sendMessage(ChatColor.GRAY + "1 199 ₽ → Основатель");
        player.sendMessage(ChatColor.GRAY + "2 499 ₽ → Легенда Chronicles");
        player.sendMessage(ChatColor.DARK_GRAY + "Донат не даёт прямого преимущества в бою.");
    }
}
