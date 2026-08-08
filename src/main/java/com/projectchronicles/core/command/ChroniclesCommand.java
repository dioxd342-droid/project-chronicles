package com.projectchronicles.core.command;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.combat.ChroniclesClass;
import com.projectchronicles.core.faction.Faction;
import com.projectchronicles.core.quest.Quest;
import com.projectchronicles.core.player.PlayerProfile;
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
        if (args.length == 0 || args[0].equalsIgnoreCase("info")) { sender.sendMessage(ChatColor.GOLD + "Project Chronicles " + ChatColor.YELLOW + plugin.getPluginMeta().getVersion()); sender.sendMessage(ChatColor.GRAY + "Твои решения меняют твою историю."); return true; }
        if (!(sender instanceof Player player)) { sender.sendMessage(ChatColor.RED + "Эта команда доступна только игроку."); return true; }
        switch (args[0].toLowerCase()) {
            case "profile" -> showProfile(player); case "class", "classes" -> classCommand(player, args); case "balance", "money" -> player.sendMessage(ChatColor.GOLD + "Баланс: " + ChatColor.YELLOW + plugin.getEconomyService().getBalance(player.getUniqueId()) + " монет");
            case "quests", "quest" -> showQuests(player); case "claim" -> claimQuest(player, args); case "choose" -> chooseFaction(player, args);
            case "factions", "reputation", "rep" -> showFactions(player); case "decision" -> showDecision(player);
            case "donate", "donation" -> plugin.getDonationService().showDonationInfo(player); case "store", "shop" -> plugin.getDonationService().showStore(player);
            case "cosmetics", "cosmetic", "collection" -> plugin.getCosmeticService().openMenu(player); case "xp" -> addTestExperience(player, args);
            case "grantcosmetic" -> grantCosmetic(player, args); case "ability", "skill" -> useAbility(player, args);
            case "progress" -> showProgress(player); case "claimprogress" -> claimProgress(player, args);
            default -> player.sendMessage(ChatColor.RED + "Использование: /chronicles [info|profile|class|balance|quests|claim|choose|factions|decision|donate|store|cosmetics|grantcosmetic|xp|ability|progress|claimprogress]");
        } return true;
    }
    private void classCommand(Player player, String[] args) {
        if (args.length == 1) {
            PlayerProfile p = plugin.getPlayerManager().getProfile(player.getUniqueId());
            player.sendMessage(ChatColor.GOLD + "=== Класс ===");
            player.sendMessage(ChatColor.YELLOW + "Текущий: " + ChatColor.WHITE + displayClass(p.getPlayerClass()));
            player.sendMessage(ChatColor.GRAY + "/chronicles class warden|hunter|seeker");
            for (ChroniclesClass c : ChroniclesClass.values()) player.sendMessage(ChatColor.YELLOW + c.name().toLowerCase() + ChatColor.GRAY + " — " + c.description());
            return;
        }
        if (plugin.getClassService().choose(player, args[1])) return;
        player.sendMessage(ChatColor.RED + "Неизвестный класс. Выбери: warden, hunter или seeker.");
    }
    private String displayClass(String id) { try { return ChroniclesClass.valueOf(id).displayName(); } catch (IllegalArgumentException ex) { return "Искатель"; } }
    private void showProfile(Player player) { PlayerProfile p = plugin.getPlayerManager().getProfile(player.getUniqueId()); long required = plugin.getExperienceService().experienceRequiredForNextLevel(p.getLevel()); player.sendMessage(ChatColor.GOLD + "=== Твой профиль ==="); player.sendMessage(ChatColor.YELLOW + "Уровень: " + ChatColor.WHITE + p.getLevel()); player.sendMessage(ChatColor.YELLOW + "Опыт: " + ChatColor.WHITE + p.getExperience() + "/" + required); player.sendMessage(ChatColor.YELLOW + "Монеты: " + ChatColor.WHITE + p.getBalance()); player.sendMessage(ChatColor.YELLOW + "Класс: " + ChatColor.WHITE + displayClass(p.getPlayerClass())); String origin = plugin.getDecisionService().get(player, "origin_faction"); player.sendMessage(ChatColor.YELLOW + "Путь: " + ChatColor.WHITE + (origin == null ? "не выбран" : origin)); }
    private void showQuests(Player player) { player.sendMessage(ChatColor.GOLD + "=== Квесты ==="); for (Quest q : plugin.getQuestService().getQuests()) { boolean done = plugin.getPlayerManager().getProfile(player.getUniqueId()).hasCompletedQuest(q.id()); player.sendMessage((done ? ChatColor.GREEN + "✓ " : ChatColor.YELLOW + "• ") + q.id() + ChatColor.WHITE + " — " + q.title()); player.sendMessage(ChatColor.GRAY + "  " + q.description()); } }
    private void claimQuest(Player player, String[] args) { if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles claim <id>"); return; } Quest q = plugin.getQuestService().getQuest(args[1]); if (q == null || !plugin.getQuestService().complete(q, player)) player.sendMessage(ChatColor.GRAY + "Квест пока недоступен или уже выполнен."); }
    private void chooseFaction(Player player, String[] args) { if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles choose <wardens|traders|seekers>"); return; } if (!plugin.getDecisionService().chooseOriginFaction(player, args[1].toLowerCase())) player.sendMessage(ChatColor.RED + "Выбор уже сделан или такой фракции нет."); }
    private void showFactions(Player player) { player.sendMessage(ChatColor.GOLD + "=== Репутация ==="); for (Faction f : plugin.getFactionService().getFactions()) { int rep = plugin.getFactionService().getReputation(player, f.id()); player.sendMessage(ChatColor.YELLOW + f.name() + ChatColor.WHITE + ": " + rep + " — " + plugin.getFactionService().getRank(rep)); } }
    private void showDecision(Player player) { String choice = plugin.getDecisionService().get(player, "origin_faction"); player.sendMessage(ChatColor.GOLD + "=== История решений ==="); player.sendMessage(ChatColor.YELLOW + "Первый путь: " + ChatColor.WHITE + (choice == null ? "ещё не выбран" : choice)); }
    private void grantCosmetic(Player player, String[] args) { if (!player.hasPermission("chronicles.admin")) { player.sendMessage(ChatColor.RED + "Недостаточно прав."); return; } if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles grantcosmetic <id>"); return; } plugin.getCosmeticService().grant(player, args[1]); }
    private void addTestExperience(Player player, String[] args) { if (!player.hasPermission("chronicles.admin")) { player.sendMessage(ChatColor.RED + "Недостаточно прав."); return; } if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles xp <amount>"); return; } try { long amount = Long.parseLong(args[1]); if (amount <= 0) throw new NumberFormatException(); plugin.getExperienceService().addExperience(player, amount); player.sendMessage(ChatColor.GREEN + "Получено " + amount + " XP."); } catch (NumberFormatException e) { player.sendMessage(ChatColor.RED + "Количество XP должно быть положительным числом."); } }
    private void useAbility(Player player, String[] args) { if (args.length < 2) { player.sendMessage(ChatColor.YELLOW + "Способности: sprint (3), guardian (5), focus (8)"); return; } plugin.getAbilityService().use(player, args[1]); }
    private void showProgress(Player player) { player.sendMessage(ChatColor.GOLD + "=== Прогресс ==="); PlayerProfile p = plugin.getPlayerManager().getProfile(player.getUniqueId()); for (var q : plugin.getProgressQuestService().getQuests()) player.sendMessage(ChatColor.YELLOW + q.id() + ChatColor.WHITE + " — " + q.title() + ": " + p.getQuestProgress(q.id()) + "/" + q.target()); }
    private void claimProgress(Player player, String[] args) { if (args.length < 2) { player.sendMessage(ChatColor.RED + "Использование: /chronicles claimprogress <id>"); return; } var q = plugin.getProgressQuestService().getQuest(args[1]); if (q == null || !plugin.getProgressQuestService().claim(q, player)) player.sendMessage(ChatColor.GRAY + "Цель ещё не выполнена или награда уже получена."); }
    @Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) { if (args.length == 1) return List.of("info","profile","class","balance","quests","claim","choose","factions","decision","donate","store","cosmetics","grantcosmetic","xp","ability","progress","claimprogress").stream().filter(v -> v.startsWith(args[0].toLowerCase())).toList(); if (args.length == 2 && args[0].equalsIgnoreCase("claim")) return plugin.getQuestService().getQuests().stream().map(Quest::id).toList(); if (args.length == 2 && args[0].equalsIgnoreCase("choose")) return plugin.getFactionService().getFactions().stream().map(Faction::id).toList(); if (args.length == 2 && args[0].equalsIgnoreCase("grantcosmetic")) return plugin.getCosmeticService().getCosmetics().stream().map(c -> c.id()).toList(); if (args.length == 2 && args[0].equalsIgnoreCase("ability")) return List.of("sprint","guardian","focus"); if (args.length == 2 && args[0].equalsIgnoreCase("class")) return List.of("warden","hunter","seeker"); if (args.length == 2 && args[0].equalsIgnoreCase("claimprogress")) return plugin.getProgressQuestService().getQuests().stream().map(q -> q.id()).toList(); return List.of(); }
}
