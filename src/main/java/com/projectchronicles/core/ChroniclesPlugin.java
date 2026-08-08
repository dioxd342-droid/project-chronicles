package com.projectchronicles.core;

import com.projectchronicles.core.command.ChroniclesCommand;
import com.projectchronicles.core.economy.EconomyService;
import com.projectchronicles.core.listener.PlayerJoinListener;
import com.projectchronicles.core.listener.PlayerQuitListener;
import com.projectchronicles.core.player.ExperienceService;
import com.projectchronicles.core.player.PlayerManager;
import com.projectchronicles.core.quest.QuestService;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChroniclesPlugin extends JavaPlugin {

    private PlayerManager playerManager;
    private ExperienceService experienceService;
    private EconomyService economyService;
    private QuestService questService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        playerManager = new PlayerManager(this);
        playerManager.initialize();
        experienceService = new ExperienceService(this);
        economyService = new EconomyService(this);
        questService = new QuestService(this);

        ChroniclesCommand command = new ChroniclesCommand(this);
        if (getCommand("chronicles") != null) {
            getCommand("chronicles").setExecutor(command);
            getCommand("chronicles").setTabCompleter(command);
        } else {
            getLogger().severe("Command 'chronicles' is missing from plugin.yml");
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getLogger().info("Project Chronicles loaded.");
    }

    @Override
    public void onDisable() {
        if (playerManager != null) playerManager.shutdown();
        getLogger().info("Project Chronicles disabled.");
    }

    public PlayerManager getPlayerManager() { return playerManager; }
    public ExperienceService getExperienceService() { return experienceService; }
    public EconomyService getEconomyService() { return economyService; }
    public QuestService getQuestService() { return questService; }
}
