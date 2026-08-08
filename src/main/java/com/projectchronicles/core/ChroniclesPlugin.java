package com.projectchronicles.core;

import com.projectchronicles.core.command.ChroniclesCommand;
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
import com.projectchronicles.core.player.ExperienceService;
import com.projectchronicles.core.player.PlayerManager;
import com.projectchronicles.core.quest.QuestService;
import com.projectchronicles.core.world.WorldEventService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChroniclesPlugin extends JavaPlugin {
    private PlayerManager playerManager;
    private ExperienceService experienceService;
    private EconomyService economyService;
    private QuestService questService;
    private FactionService factionService;
    private NpcService npcService;
    private WorldEventService worldEventService;
    private DecisionService decisionService;
    private DonationService donationService;
    private CosmeticService cosmeticService;

    @Override public void onEnable() {
        saveDefaultConfig();
        playerManager = new PlayerManager(this); playerManager.initialize();
        experienceService = new ExperienceService(this);
        economyService = new EconomyService(this);
        questService = new QuestService(this);
        factionService = new FactionService(this);
        npcService = new NpcService(this);
        worldEventService = new WorldEventService(this);
        decisionService = new DecisionService(this);
        donationService = new DonationService(this);
        cosmeticService = new CosmeticService(this);

        ChroniclesCommand command = new ChroniclesCommand(this);
        if (getCommand("chronicles") != null) {
            getCommand("chronicles").setExecutor(command);
            getCommand("chronicles").setTabCompleter(command);
        } else getLogger().severe("Command 'chronicles' is missing from plugin.yml");

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerActivityListener(this), this);
        getServer().getPluginManager().registerEvents(new NpcInteractionListener(this, npcService), this);
        npcService.spawnStorytellerIfNeeded();
        worldEventService.start();
        getLogger().info("Project Chronicles loaded.");
    }

    @Override public void onDisable() {
        if (worldEventService != null) worldEventService.stop();
        if (playerManager != null) playerManager.shutdown();
        getLogger().info("Project Chronicles disabled.");
    }
    public PlayerManager getPlayerManager() { return playerManager; }
    public ExperienceService getExperienceService() { return experienceService; }
    public EconomyService getEconomyService() { return economyService; }
    public QuestService getQuestService() { return questService; }
    public FactionService getFactionService() { return factionService; }
    public WorldEventService getWorldEventService() { return worldEventService; }
    public DecisionService getDecisionService() { return decisionService; }
    public DonationService getDonationService() { return donationService; }
    public CosmeticService getCosmeticService() { return cosmeticService; }
}
