package com.projectchronicles.core.command;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.quest.Quest;
import com.projectchronicles.core.player.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class ChroniclesCommand implements CommandExecutor, TabCompleter {

    private final ChroniclesPlugin plugin;

    public ChroniclesCommand(ChroniclesPlugin plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(ChatColor.GOLD + "Project Chronicles " + ChatColor.YELLOW + plugin.getPluginMeta().getVersion());
            sender.sendMessage(ChatColor.GRAY + "Мир меняется вместе с действиями игроков.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Эта команда доступна только игроку.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "profile" -> showProfile(player);
            case "balance", "money" -> player.sendMessage(ChatColor.GOLD + "Баланс: " + ChatColor.YELLOW + plugin.getEconomyService().getBalance(player.getUniqueId()) + " монет");
            case "quests", "quest" -> showQuests(player);
            case "claim" -> claimQuest(player, args);
            case "xp" -> addTestExperience(player, args);
            default -> player.sendMessage(ChatColor.RED + "Использование: /chronicles [info|profile|balance|quests|claim <id>|xp <amount>]");
        }
        return true;
    }

    private void showProfile(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        long required = plugin.getExperienceService().experienceRequiredForNextLevel(profile.getLevel());
        player.sendMessage(ChatColor.GOLD + "=== Твой профиль ===");
        player.sendMessage(ChatColor.YELLOW + "Уровень: " + ChatColor.WHITE + profile.getLevel());
        player.sendMessage(ChatColor.YELLOW + "Опыт: " + ChatColor.WHITE + profile.getExperience() + "/" + required);
        player.sendMessage(ChatColor.YELLOW + "Монеты: " + ChatColor.WHITE + profile.getBalance());
    }

    private void showQuests(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Квесты ===");
        for (Quest quest : plugin.getQuestService().getQuests()) {
            boolean completed = plugin.getPlayerManager().getProfile(player.getUniqueId()).hasCompletedQuest(quest.id());
            player.sendMessage((completed ? ChatColor.GREEN + "✓ " : ChatColor.YELLOW + "• ") + quest.id() + ChatColor.WHITE + " — " + quest.title());
            player.sendMessage(ChatColor.GRAY + "  " + quest.description());
        }
    }

    private void claimQuest(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Использование: /chronicles claim <id>");
            return;
        }
        Quest quest = plugin.getQuestService().getQuest(args[1]);
        if (quest == null) {
            player.sendMessage(ChatColor.RED + "Такого квеста нет.");
            return;
        }
        if (!plugin.getQuestService().complete(quest, player)) {
            player.sendMessage(ChatColor.GRAY + "Квест пока недоступен или уже выполнен.");
        }
    }

    private void addTestExperience(Player player, String[] args) {
        if (!player.hasPermission("chronicles.admin")) {
            player.sendMessage(ChatColor.RED + "Недостаточно прав.");
            return;
        }
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Использование: /chronicles xp <amount>");
            return;
        }
        try {
            long amount = Long.parseLong(args[1]);
            plugin.getExperienceService().addExperience(player, amount);
            player.sendMessage(ChatColor.GREEN + "Получено " + amount + " XP.");
        } catch (NumberFormatException exception) {
            player.sendMessage(ChatColor.RED + "Количество XP должно быть числом.");
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> values = List.of("info", "profile", "balance", "quests", "claim", "xp");
            return values.stream().filter(v -> v.startsWith(args[0].toLowerCase())).toList();
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("claim")) {
            return plugin.getQuestService().getQuests().stream().map(Quest::id).toList();
        }
        return List.of();
    }
}
