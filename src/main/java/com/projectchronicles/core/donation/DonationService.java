package com.projectchronicles.core.donation;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class DonationService {

    public record Product(String id, String name, String description, String price, String command) {}

    private final ChroniclesPlugin plugin;
    private final List<Product> products = List.of(
            new Product("chronikler", "Хроникёр", "Уникальный титул и косметический префикс.", "4.99", "lp user %player% meta setprefix 10 \"&6[Хроникёр] &r\""),
            new Product("guardian", "Хранитель", "Расширенный набор косметических эффектов и титулов.", "9.99", "lp user %player% meta setprefix 20 \"&b[Хранитель] &r\""),
            new Product("founder", "Основатель", "Эксклюзивный статус основателя проекта.", "19.99", "lp user %player% meta setprefix 30 \"&d[Основатель] &r\"")
    );

    public DonationService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void showStore(Player player) {
        String url = plugin.getConfig().getString("donation.store-url", "");
        player.sendMessage(ChatColor.GOLD + "=== Chronicles Store ===");
        for (Product product : products) {
            player.sendMessage(ChatColor.YELLOW + product.name() + ChatColor.WHITE + " — " + product.price() + " EUR");
            player.sendMessage(ChatColor.GRAY + "  " + product.description());
        }
        if (url.isBlank()) {
            player.sendMessage(ChatColor.RED + "Магазин ещё не подключён.");
        } else {
            player.sendMessage(ChatColor.GREEN + "Магазин: " + ChatColor.WHITE + url);
        }
    }

    public void showDonationInfo(Player player) {
        player.sendMessage(ChatColor.GOLD + "Донат Chronicles");
        player.sendMessage(ChatColor.GRAY + "Покупки не должны давать прямого преимущества в бою.");
        player.sendMessage(ChatColor.GRAY + "Основной акцент: титулы, косметика и поддержка проекта.");
    }
}
