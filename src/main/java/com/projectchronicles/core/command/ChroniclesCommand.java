package com.projectchronicles.core.command;

import com.projectchronicles.core.ChroniclesPlugin;
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

    public ChroniclesCommand(ChroniclesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(ChatColor.GOLD + "Project Chronicles " + ChatColor.YELLOW + plugin.getPluginMeta().getVersion());
            sender.sendMessage(ChatColor.GRAY + "Мир меняется вместе с действиями игроков.");
            return true;
        }

        if (args[0].equalsIgnoreCase("profile")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(ChatColor.RED + "Эта команда доступна только игроку.");
                return true;
            }

            PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
            sender.sendMessage(ChatColor.GOLD + "=== Твой профиль ===");
            sender.sendMessage(ChatColor.YELLOW + "Уровень: " + ChatColor.WHITE + profile.getLevel());
            sender.sendMessage(ChatColor.YELLOW + "Опыт: " + ChatColor.WHITE + profile.getExperience());
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Использование: /chronicles [info|profile]");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> result = new ArrayList<>();
            result.add("info");
            result.add("profile");
            return result.stream()
                    .filter(value -> value.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
