package com.projectchronicles.core.command;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.faction.Faction;
import com.projectchronicles.core.player.PlayerProfile;
import com.projectchronicles.core.quest.ProgressQuestService;
import com.projectchronicles.core.quest.Quest;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public final class ChroniclesCommand implements CommandExecutor, TabCompleter {
    private final ChroniclesPlugin plugin;
    public ChroniclesCommand(ChroniclesPlugin plugin) { this.plugin = plugin; }

    @Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(ChatColor.GOLD + "Project Chronicles " + ChatColor.YELLOW + plugin.getPluginMeta().getVersion());
            sender.sendMessage(ChatColor.GRAY + "Твои решения меняют твою историю.");
            return true;
        }
        if (!(sender instanceof Player player)) { sender.sendMessage(ChatColor.RED + "Эта команда доступна только игроку."); return true; }
        switch (args[0].toLowerCase()) {
            case "profile" -> showProfile(player);
            case "balance", "money" -> player.sendMessage(ChatColor.GOLD + "Баланс: " + ChatColor.YELLOW + plugin.getEconomyService().getBalance(player.getUniqueId()) + " монет");
            case "quests", "quest" -> showQuests(player);
            case "progress" -> showProgressQuests(player);
            case "claim" -> claimQuest(player, args);
            case "claimprogress" -> claimProgressQuest(player, args);
            case "choose" -> chooseFaction(player, args);
            case "factions", "reputation", "rep" -> showFactions(player);
            case "decision" -> showDecision(player);
            case "donate", "donation" -> plugin.getDonationService().showDonationInfo(player);
            case "store", "shop" -> plugin.getDonationService().showStore(player);
            case "cosmetics", "cosmetic", "collection" -> plugin.getCosmeticService().openMenu(player);
            case "xp" -> addTestExperience(player, args);
            case "grantcosmetic" -> grantCosmetic(player, args);
            default -> player.sendMessage(ChatColor.RED + "Использование: /chronicles [info|profile|balance|quests|progress|claim|claimprogress|choose|factions|decision|donate|store|cosmetics|grantcosmetic|xp]");
        }
        return true;
    }

    private void showProfile(Player player) {
        PlayerProfile p = plugin.getPlayerManager().getProfile(player.getUniqueId());
        long required = plugin.getExperienceService().experienceRequiredForNextLevel(p.getLevel());
        player.sendMessage(ChatColor.GOLD + "=== Твой профиль ===");
        player.sendMessage(ChatColor.YELLOW + "Уровень: " + ChatColor.WHITE + p.getLevel());
        player.sendMessage(ChatColor.YELLOW + "Опыт: " + ChatColor.WHITE + p.getExperience() + "/" + required);
        player.sendMessage(ChatColor.YELLOW + "Монеты: " + ChatColor.WHITE + p.getBalance());
        String origin = plugin.getDecisionService().get(player, "origin_faction");
        player.sendMessage(ChatColor.YELLOW + "Путь: " + ChatColor.WHITE + (origin == null ? "не выбран" : origin));
    }

    private void showQuests(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Сюжетные квесты ===");
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        for (Quest q : plugin.getQuestService().getQuests()) {
            boolean done = profile.hasCompletedQuest(q.id());
            player.sendMessage((done ? ChatColor.GREEN + "✓ " : ChatColor.YELLOW + "• ") + q.id() + ChatColor.WHITE + " — " + q.title());
            player.sendMessage(ChatColor.GRAY + "  " + q.description());
        }
    }

    private void showProgressQuests(Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "=== Активные цели ===");
        for (ProgressQuestService.Definition q : plugin.getProgressQuestService().getQuests()) {
            int progress = profile.getQuestProgress(q.id());
            boolean done = profile.hasCompletedQuest(q.id());
            player.sendMessage((done ? ChatColor.GREEN + "✓ " : ChatColor.YELLOW + "• ") + q.title() + ChatColor.WHITE + " — " + progress + "/" + q.target());
            player.sendMessage(ChatColor.GRAY + "  " + q.description());
        }
    }

    private void claimQuest(Player player, String[] args) {
        if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles claim <id>"); return; }
        Quest q = plugin.getQuestService().getQuest(args[1]);
        if (q == null || !plugin.getQuestService().complete(q, player)) player.sendMessage(ChatColor.GRAY + "Квест пока недоступен или уже выполнен.");
    }

    private void claimProgressQuest(Player player, String[] args) {
        if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles claimprogress <id>"); return; }
        ProgressQuestService.Definition q = plugin.getProgressQuestService().getQuest(args[1]);
        if (q == null || !plugin.getProgressQuestService().claim(q, player)) player.sendMessage(ChatColor.GRAY + "Цель ещё не выполнена или уже получена.");
    }

    private void chooseFaction(Player player, String[] args) {
        if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles choose <wardens|traders|seekers>"); return; }
        if (!plugin.getDecisionService().chooseOriginFaction(player, args[1].toLowerCase())) player.sendMessage(ChatColor.RED + "Выбор уже сделан или такой фракции нет.");
    }

    private void showFactions(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== Репутация ===");
        for (Faction f : plugin.getFactionService().getFactions()) {
            int rep = plugin.getFactionService().getReputation(player, f.id());
            player.sendMessage(ChatColor.YELLOW + f.name() + ChatColor.WHITE + ": " + rep + " — " + plugin.getFactionService().getRank(rep));
        }
    }

    private void showDecision(Player player) {
        String choice = plugin.getDecisionService().get(player, "origin_faction");
        player.sendMessage(ChatColor.GOLD + "=== История решений ===");
        player.sendMessage(ChatColor.YELLOW + "Первый путь: " + ChatColor.WHITE + (choice == null ? "ещё не выбран" : choice));
    }

    private void grantCosmetic(Player player, String[] args) {
        if (!player.hasPermission("chronicles.admin")) { player.sendMessage(ChatColor.RED + "Недостаточно прав."); return; }
        if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles grantcosmetic <id>"); return; }
        plugin.getCosmeticService().grant(player, args[1]);
    }

    private void addTestExperience(Player player, String[] args) {
        if (!player.hasPermission("chronicles.admin")) { player.sendMessage(ChatColor.RED + "Недостаточно прав."); return; }
        if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles xp <amount>"); return; }
        try {
            long amount = Long.parseLong(args[1]);
            if (amount <= 0) throw new NumberFormatException();
            plugin.getExperienceService().addExperience(player, amount);
            player.sendMessage(ChatColor.GREEN + "Получено " + amount + " XP.");
        } catch (NumberFormatException e) { player.sendMessage(ChatColor.RED + "Количество XP должно быть положительным числом."); }
    }

    @Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return List.of("info","profile","balance","quests","progress","claim","claimprogress","choose","factions","decision","donate","store","cosmetics","grantcosmetic","xp").stream().filter(v -> v.startsWith(args[0].toLowerCase())).toList();
        if (args.length == 2 && args[0].equalsIgnoreCase("claim")) return plugin.getQuestService().getQuests().stream().map(Quest::id).toList();
        if (args.length == 2 && args[0].equalsIgnoreCase("claimprogress")) return plugin.getProgressQuestService().getQuests().stream().map(ProgressQuestService.Definition::id).toList();
        if (args.length == 2 && args[0].equalsIgnoreCase("choose")) return plugin.getFactionService().getFactions().stream().map(Faction::id).toList();
        if (args.length == 2 && args[0].equalsIgnoreCase("grantcosmetic")) return plugin.getCosmeticService().getCosmetics().stream().map(c -> c.id()).toList();
        return List.of();
    }
}
