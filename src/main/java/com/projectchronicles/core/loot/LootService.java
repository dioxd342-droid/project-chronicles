package com.projectchronicles.core.loot;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.concurrent.ThreadLocalRandom;

public final class LootService {
    public void roll(Player player, EntityType type) {
        int roll = ThreadLocalRandom.current().nextInt(100);
        if (roll >= chance(type)) return;

        ItemStack item = createLoot(type, roll);
        player.getInventory().addItem(item).values().forEach(stack -> player.getWorld().dropItemNaturally(player.getLocation(), stack));
        player.sendMessage(ChatColor.GOLD + "✦ Добыча: " + ChatColor.YELLOW + item.getItemMeta().getDisplayName());
    }

    private int chance(EntityType type) {
        return switch (type) {
            case ZOMBIE, SKELETON, SPIDER, HUSK -> 18;
            case CREEPER, ENDERMAN, WITCH -> 28;
            default -> 8;
        };
    }

    private ItemStack createLoot(EntityType type, int roll) {
        Material material;
        String name;
        if (type == EntityType.SKELETON || type == EntityType.CREEPER) {
            material = Material.BOW;
            name = "Лук Пепельного охотника";
        } else if (type == EntityType.SPIDER) {
            material = Material.LEATHER_CHESTPLATE;
            name = "Панцирь паучьего следопыта";
        } else if (type == EntityType.ENDERMAN) {
            material = Material.ENDER_PEARL;
            name = "Осколок Пустоты";
        } else {
            material = Material.IRON_SWORD;
            name = "Клинок хроникёра";
        }

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + name);
        meta.setLore(java.util.List.of(ChatColor.GRAY + "Уникальная добыча Project Chronicles", ChatColor.DARK_GRAY + "ID: chronicles_loot"));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (roll < 5 && material != Material.ENDER_PEARL) meta.addEnchant(Enchantment.UNBREAKING, 1, true);
        item.setItemMeta(meta);
        return item;
    }
}
