package com.projectchronicles.core.quest;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public final class QuestService {
    private final ChroniclesPlugin plugin;
    private final List<Quest> quests = List.of(
            new Quest("first_steps", "Первые шаги", "Войди в мир Chronicles и начни своё приключение.", 50, 25),
            new Quest("survivor", "Выживший", "Достигни второго уровня.", 100, 75),
            new Quest("warden_oath", "Клятва Стражей", "Выбери путь Стражей и подтверди свою верность.", 150, 120),
            new Quest("trader_oath", "Караванный договор", "Выбери путь Вольных торговцев и подтверди свою независимость.", 150, 160),
            new Quest("seeker_path", "След древних", "Выбери Искателей и начни поиски утраченных хроник.", 200, 100)
    );

    public QuestService(ChroniclesPlugin plugin) { this.plugin = plugin; }
    public List<Quest> getQuests() { return quests; }
    public Quest getQuest(String id) { return quests.stream().filter(q -> q.id().equalsIgnoreCase(id)).findFirst().orElse(null); }

    public boolean canComplete(Quest quest, Player player) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (quest == null || profile.hasCompletedQuest(quest.id())) return false;
        String path = plugin.getDecisionService().get(player, "origin_faction");
        return switch (quest.id()) {
            case "first_steps" -> true;
            case "survivor" -> profile.getLevel() >= 2;
            case "warden_oath" -> "wardens".equals(path) && plugin.getFactionService().getReputation(player, "wardens") >= 100;
            case "trader_oath" -> "traders".equals(path) && plugin.getFactionService().getReputation(player, "traders") >= 100;
            case "seeker_path" -> "seekers".equals(path) && plugin.getFactionService().getReputation(player, "seekers") >= 100;
            default -> false;
        };
    }

    public boolean complete(Quest quest, Player player) {
        if (!canComplete(quest, player)) return false;
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        profile.completeQuest(quest.id());
        plugin.getExperienceService().addExperience(player, quest.experienceReward());
        plugin.getEconomyService().deposit(player.getUniqueId(), quest.moneyReward());
        plugin.getPlayerManager().saveProfile(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "✦ Квест выполнен: " + ChatColor.YELLOW + quest.title());
        player.sendMessage(ChatColor.GRAY + "+" + quest.experienceReward() + " XP  +" + quest.moneyReward() + " монет");
        return true;
    }
}
