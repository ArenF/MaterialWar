package com.aren.materialwar;

import com.aren.ability.events.AbilityListener;
import com.aren.commands.MaterialWarExecutor;
import com.aren.commands.MaterialWarTabCompleter;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.event.GameEventListener;
import com.aren.game.state.GameState;
import com.aren.utils.player.PlayerState;
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
            gameConfiguration.set("game.state", GameState.WAITING.name());
            gameConfiguration.set("game.time", "30m 0s");
            gameConfiguration.set("game.invulnerable_time", "10m 0s");
            gameConfiguration.set("game.invulnerable", true);
            gameConfiguration.set("game.worldBorder.move_enable", false); //월드보더 움직임 활성화
            gameConfiguration.set("game.worldBorder.damage", 2.0); //월드보더 데미지 설정
            gameConfiguration.set("game.worldBorder.buffer", 10.0); // 월드보더 buffer 거리를 벗어나면 데미지 증가
            gameConfiguration.set("game.worldBorder.default_size", 125.0); //기본 사이즈 설정
            gameConfiguration.set("game.worldBorder.reduce_count", 5); //줄어드는 횟수 설정
            gameConfiguration.set("game.worldBorder.reduce_distance", 10.0); //줄어드는 거리 설정
            gameConfiguration.set("game.worldBorder.reduce_duration", 30.0); //줄어드는 시간 설정

            gameConfiguration.set("game.deathMessage", "player가 killer에게 살해당하였습니다.");
            gameConfiguration.createSection("game.startLocation");
            gameConfiguration.createSection("game.team");
            gameConfiguration.createSection("game.default_item");
            gameConfig.save();
        }
        ConfigFile skillConfig = new ConfigFile(plugin, "SkillConfig");
        if (skillConfig.isEmpty()) {
            FileConfiguration skillConfiguration = skillConfig.getConfig();
            skillConfiguration.set("emerald.duration", 3.0);
            skillConfiguration.set("emerald.cooltime", 15.0);
            skillConfiguration.set("diamond.damage", 5.0);
            skillConfiguration.set("diamond.length", 15.0);
            skillConfiguration.set("diamond.cooltime", 5);
            skillConfiguration.set("iron.level_interval", 5);
            skillConfiguration.set("iron.cooltime", 15);
            skillConfiguration.set("gold.heal", 1.0);
            skillConfiguration.set("gold.removeEffectCost", 8);
            skillConfiguration.set("gold.cooltime", 8);
            skillConfiguration.set("lapis.gainBuffInterval", 16);
            skillConfiguration.set("lapis.duration", 30);
            skillConfiguration.set("lapis.level_divided", 8);
            skillConfiguration.set("lapis.cooltime", 6);
            skillConfiguration.set("redstone.range", 2.0);
            skillConfiguration.set("redstone.cost_interval", 8);
            skillConfiguration.set("redstone.damage", 2.0);
            skillConfiguration.set("redstone.slowness", 3);
            skillConfiguration.set("redstone.duration", 8);
            skillConfiguration.set("redstone.cooltime", 6);
            skillConfiguration.set("coal.damage", 3.0);
            skillConfiguration.set("coal.velocity", 2.0);
            skillConfiguration.set("coal.cooltime", 4);
            skillConfiguration.set("coal.deletedDistance", 50.0);
            skillConfiguration.set("stone.damage", 4.0);
            skillConfiguration.set("stone.velocity", 1.0);
            skillConfiguration.set("stone.knockback", 4.0);
            skillConfiguration.set("stone.cooltime", 2);
            skillConfig.save();
        }

        ConfigFile playerConfig = new ConfigFile(plugin, "PlayerConfig");
        for (PlayerState state : PlayerState.values()) {
            playerConfig.getConfig().createSection(state.name());
        }
        playerConfig.save();

        configManager.addConfigFile("gameConfig", gameConfig);
        configManager.addConfigFile("skillConfig", skillConfig);
        configManager.addConfigFile("playerConfig", playerConfig);

        Objects.requireNonNull(Bukkit.getPluginCommand("MaterialWar")).setExecutor(new MaterialWarExecutor());
        Objects.requireNonNull(Bukkit.getPluginCommand("MaterialWar")).setTabCompleter(new MaterialWarTabCompleter());
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
