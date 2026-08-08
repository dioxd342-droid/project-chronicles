package com.projectchronicles.core;

import com.projectchronicles.core.command.ChroniclesCommand;
import com.projectchronicles.core.listener.PlayerJoinListener;
import com.projectchronicles.core.player.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChroniclesPlugin extends JavaPlugin {

    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.playerManager = new PlayerManager(this);
        this.playerManager.initialize();

        ChroniclesCommand command = new ChroniclesCommand(this);
        if (getCommand("chronicles") != null) {
            getCommand("chronicles").setExecutor(command);
            getCommand("chronicles").setTabCompleter(command);
        } else {
            getLogger().severe("Command 'chronicles' is missing from plugin.yml");
        }

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getLogger().info("Project Chronicles loaded.");
    }

    @Override
    public void onDisable() {
        if (playerManager != null) {
            playerManager.shutdown();
        }
        getLogger().info("Project Chronicles disabled.");
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }
}
