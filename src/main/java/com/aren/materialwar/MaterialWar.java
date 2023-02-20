package com.aren.materialwar;

import com.aren.ability.events.AbilityListener;
import com.aren.commands.MaterialWarExecutor;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.event.GameEventListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class MaterialWar extends JavaPlugin {

    private static Plugin plugin;
    private static ConfigManager configManager = ConfigManager.getInstance();
    private static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;


        // 콘피그 작성
        ConfigFile gameConfig = new ConfigFile(plugin, "GameConfig");
        if (gameConfig.isEmpty()) {
            FileConfiguration gameConfiguration = gameConfig.getConfig();
            gameConfiguration.set("game.time", "30m 0s");
            gameConfiguration.set("game.invulnerable_time", "5m 0s");
            gameConfiguration.set("game.invulnerable", true);
            gameConfiguration.set("game.worldBorder.enable", true);
            gameConfiguration.set("game.worldBorder.damage", 2.0);
            gameConfiguration.set("game.worldBorder.buffer", 10.0);
            gameConfiguration.set("game.worldBorder.default_size", 60.0);
            gameConfiguration.set("game.worldBorder.reduce_distance", 10.0);


            gameConfiguration.set("game.deathMessage", "player가 killer에게 살해당하였습니다.");
            gameConfig.save();
        }

        ConfigFile skillConfig = new ConfigFile(plugin, "SkillConfig");
        if (skillConfig.isEmpty()) {
            FileConfiguration skillConfiguration = skillConfig.getConfig();
            skillConfiguration.set("diamond.damage", 2.0);
            skillConfiguration.set("diamond.length", 10.0);
            skillConfiguration.set("diamond.cooltime", 5);
            skillConfiguration.set("iron.level_interval", 5);
            skillConfiguration.set("iron.cooltime", 15);
            skillConfiguration.set("gold.heal", 2.0);
            skillConfiguration.set("gold.cooltime", 8);
            skillConfiguration.set("stone.damage", 4.0);
            skillConfiguration.set("stone.knockback", 4.0);
            skillConfiguration.set("stone.cooltime", 2);
            skillConfig.save();
        }
        configManager.addConfigFile("gameConfig", gameConfig);
        configManager.addConfigFile("skillConfig", skillConfig);

        Objects.requireNonNull(Bukkit.getPluginCommand("MaterialWar")).setExecutor(new MaterialWarExecutor());
        Bukkit.getServer().getPluginManager().registerEvents(new GameEventListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new AbilityListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }

}
