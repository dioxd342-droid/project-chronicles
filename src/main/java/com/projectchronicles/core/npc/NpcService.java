package com.projectchronicles.core.npc;

import com.projectchronicles.core.ChroniclesPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

public final class NpcService {
    private final ChroniclesPlugin plugin;
    private final NamespacedKey npcKey;

    public NpcService(ChroniclesPlugin plugin) {
        this.plugin = plugin;
        this.npcKey = new NamespacedKey(plugin, "chronicles_npc");
    }

    public void spawnStorytellerIfNeeded() {
        if (!plugin.getConfig().getBoolean("npc.storyteller.enabled", true)) return;
        World world = plugin.getServer().getWorlds().getFirst();
        Location location = world.getSpawnLocation().clone().add(2.5, 0, 2.5);
        for (Villager villager : world.getEntitiesByClass(Villager.class)) {
            if ("storyteller".equals(villager.getPersistentDataContainer().get(npcKey, PersistentDataType.STRING))) return;
        }
        Villager npc = (Villager) world.spawnEntity(location, EntityType.VILLAGER);
        npc.setCustomName("§6Элиан — Хранитель хроник");
        npc.setCustomNameVisible(true);
        npc.setInvulnerable(true);
        npc.setAI(false);
        npc.setProfession(Villager.Profession.LIBRARIAN);
        npc.getPersistentDataContainer().set(npcKey, PersistentDataType.STRING, "storyteller");
    }

    public boolean isStoryteller(Villager villager) {
        return "storyteller".equals(villager.getPersistentDataContainer().get(npcKey, PersistentDataType.STRING));
    }
}
