package com.projectchronicles.core;

import com.projectchronicles.core.combat.ClassService;
import com.projectchronicles.core.combat.CombatListener;
import com.projectchronicles.core.command.ChroniclesCommand;
import com.projectchronicles.core.cosmetic.CosmeticListener;
import com.projectchronicles.core.cosmetic.CosmeticPetService;
import com.projectchronicles.core.cosmetic.CosmeticService;
import com.projectchronicles.core.decision.DecisionService;
import com.projectchronicles.core.donation.DonationService;
import com.projectchronicles.core.economy.EconomyService;
import com.projectchronicles.core.faction.FactionService;
import com.projectchronicles.core.listener.PlayerActivityListener;
import com.projectchronicles.core.listener.PlayerJoinListener;
import com.projectchronicles.core.listener.PlayerQuitListener;
import com.projectchronicles.core.npc.NpcInteractionListener;
import com.projectchronicles.core.npc.NpcService;
import com.projectchronicles.core.player.AbilityService;
import com.projectchronicles.core.player.ExperienceService;
import com.projectchronicles.core.player.PlayerManager;
import com.projectchronicles.core.quest.ProgressQuestListener;
import com.projectchronicles.core.quest.ProgressQuestService;
import com.projectchronicles.core.quest.QuestService;
import com.projectchronicles.core.world.WorldEventService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChroniclesPlugin extends JavaPlugin {
    private PlayerManager playerManager; private ExperienceService experienceService; private AbilityService abilityService; private ClassService classService; private EconomyService economyService;
    private QuestService questService; private ProgressQuestService progressQuestService; private FactionService factionService; private NpcService npcService;
    private WorldEventService worldEventService; private DecisionService decisionService; private DonationService donationService;
    private CosmeticService cosmeticService; private CosmeticPetService cosmeticPetService;

    @Override public void onEnable() {
        saveDefaultConfig(); playerManager = new PlayerManager(this); playerManager.initialize();
        experienceService = new ExperienceService(this); abilityService = new AbilityService(this); classService = new ClassService(this); economyService = new EconomyService(this);
        questService = new QuestService(this); progressQuestService = new ProgressQuestService(this); factionService = new FactionService(this); npcService = new NpcService(this);
        worldEventService = new WorldEventService(this); decisionService = new DecisionService(this); donationService = new DonationService(this);
        cosmeticService = new CosmeticService(this); cosmeticPetService = new CosmeticPetService(this);
        ChroniclesCommand command = new ChroniclesCommand(this);
        if (getCommand("chronicles") != null) { getCommand("chronicles").setExecutor(command); getCommand("chronicles").setTabCompleter(command); } else getLogger().severe("Command 'chronicles' is missing from plugin.yml");
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerActivityListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new NpcInteractionListener(this, npcService), this);
        getServer().getPluginManager().registerEvents(new CosmeticListener(this, cosmeticService), this);
        getServer().getPluginManager().registerEvents(new ProgressQuestListener(this, progressQuestService), this);
        npcService.spawnStorytellerIfNeeded(); worldEventService.start();
        getServer().getScheduler().runTaskTimer(this, cosmeticService::tickEffects, 20L, 10L);
        getServer().getScheduler().runTaskTimer(this, cosmeticPetService::tick, 20L, 10L);
        getLogger().info("Project Chronicles loaded.");
    }
    @Override public void onDisable() { if (worldEventService != null) worldEventService.stop(); if (cosmeticPetService != null) cosmeticPetService.shutdown(); if (playerManager != null) playerManager.shutdown(); }
    public PlayerManager getPlayerManager() { return playerManager; } public ExperienceService getExperienceService() { return experienceService; } public AbilityService getAbilityService() { return abilityService; } public ClassService getClassService() { return classService; }
    public EconomyService getEconomyService() { return economyService; } public QuestService getQuestService() { return questService; } public ProgressQuestService getProgressQuestService() { return progressQuestService; }
    public FactionService getFactionService() { return factionService; } public WorldEventService getWorldEventService() { return worldEventService; } public DecisionService getDecisionService() { return decisionService; }
    public DonationService getDonationService() { return donationService; } public CosmeticService getCosmeticService() { return cosmeticService; } public CosmeticPetService getCosmeticPetService() { return cosmeticPetService; }
}
