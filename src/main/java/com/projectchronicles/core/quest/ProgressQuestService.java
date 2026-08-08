package com.projectchronicles.core.quest;

import com.projectchronicles.core.ChroniclesPlugin;
import com.projectchronicles.core.player.PlayerProfile;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

public final class ProgressQuestService {
    public enum Type { MINE, HUNT }
    public record Definition(String id, String title, String description, Type type, int target, long xp, long money) {}

    private final ChroniclesPlugin plugin;
    private final List<Definition> quests = List.of(
            new Definition("miner_apprentice", "Ученик шахтёра", "Добудь 32 каменных блока.", Type.MINE, 32, 150, 100),
            new Definition("monster_hunter", "Охотник на чудовищ", "Победи 10 враждебных существ.", Type.HUNT, 10, 250, 180),
            new Definition("master_miner", "Мастер шахты", "Добудь 128 каменных блоков.", Type.MINE, 128, 600, 450)
    );

    private static final Set<Material> MINE_BLOCKS = Set.of(
            Material.STONE, Material.COBBLESTONE, Material.DEEPSLATE,
            Material.COAL_ORE, Material.IRON_ORE, Material.COPPER_ORE,
            Material.GOLD_ORE, Material.DIAMOND_ORE, Material.REDSTONE_ORE
    );

    private static final Set<EntityType> HOSTILES = Set.of(
            EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER,
            EntityType.CREEPER, EntityType.ENDERMAN, EntityType.WITCH,
            EntityType.HUSK, EntityType.DROWNED, EntityType.STRAY,
            EntityType.PILLAGER, EntityType.VINDICATOR, EntityType.PHANTOM
    );

    public ProgressQuestService(ChroniclesPlugin plugin) { this.plugin = plugin; }
    public List<Definition> getQuests() { return quests; }

    public void onMine(Player player, Material material) {
        if (!MINE_BLOCKS.contains(material)) return;
        increment(player, Type.MINE, 1);
    }

    public void onKill(Player player, EntityType type) {
        if (!HOSTILES.contains(type)) return;
        increment(player, Type.HUNT, 1);
    }

    private void increment(Player player, Type type, int amount) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        for (Definition quest : quests) {
            if (quest.type() != type || profile.hasCompletedQuest(quest.id())) continue;
            int old = profile.getQuestProgress(quest.id());
            int next = Math.min(quest.target(), old + amount);
            profile.setQuestProgress(quest.id(), next);
            if (old < quest.target() && next >= quest.target()) {
                player.sendMessage(ChatColor.GOLD + "✦ Цель выполнена: " + ChatColor.YELLOW + quest.title());
                player.sendMessage(ChatColor.GRAY + "Используй /chronicles claimprogress " + quest.id());
            }
        }
    }

    public boolean claim(Definition quest, Player player) {
        if (quest == null) return false;
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player.getUniqueId());
        if (profile.hasCompletedQuest(quest.id()) || profile.getQuestProgress(quest.id()) < quest.target()) return false;
        profile.completeQuest(quest.id());
        plugin.getExperienceService().addExperience(player, quest.xp());
        plugin.getEconomyService().deposit(player.getUniqueId(), quest.money());
        plugin.getPlayerManager().saveProfile(player.getUniqueId());
        player.sendMessage(ChatColor.GOLD + "✦ Квест выполнен: " + ChatColor.YELLOW + quest.title());
        player.sendMessage(ChatColor.GRAY + "+" + quest.xp() + " XP  +" + quest.money() + " монет");
        return true;
    }

    public Definition getQuest(String id) {
        return quests.stream().filter(q -> q.id().equalsIgnoreCase(id)).findFirst().orElse(null);
    }
}
