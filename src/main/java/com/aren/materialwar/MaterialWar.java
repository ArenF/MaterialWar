package com.aren.materialwar;

import com.aren.commands.MaterialWarExecutor;
import com.aren.commands.MaterialWarTabCompleter;
import com.aren.events.EventListener;
import com.aren.events.EventManager;
import com.aren.utils.data.GameConfig;
import com.aren.utils.data.SkillConfig;
import com.aren.utils.gameManage.GameManager;
import com.aren.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaterialWar extends JavaPlugin {

    private static JavaPlugin plugin = null;
    private static GameManager gameManager = GameManager.getInstance();
    private static ConfigManager configManager = ConfigManager.getInstance();
    private static EventManager eventManager = EventManager.getInstance();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        plugin.getCommand("MaterialGame").setExecutor(new MaterialWarExecutor());
        plugin.getCommand("MaterialGame").setTabCompleter(new MaterialWarTabCompleter());
        Bukkit.getPluginManager().registerEvents(new EventListener(), plugin);
        plugin.getLogger().info("Hello world!");
        configManager.createConfigFile("SkillConfig", new SkillConfig(plugin));
        configManager.createConfigFile("GameConfig", new GameConfig(plugin));
        configManager.configDataSet();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}
