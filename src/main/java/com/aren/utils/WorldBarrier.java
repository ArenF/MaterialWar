package com.aren.utils;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class WorldBarrier {

    private ConfigFile gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");

    private int taskId = -1;
    private double damage;
    private double buffer;
    private double defaultSize;
    private double reduceDistance;
    private int reduceCount;
    private long reduceDuration;
    TimerBar bar;
    WorldBorder worldBorder;

    public WorldBarrier(Location center, TimerBar timerBar) {
        damage = gameConfig.getConfig().getDouble("game.worldBorder.damage");
        buffer = gameConfig.getConfig().getDouble("game.worldBorder.buffer");
        defaultSize = gameConfig.getConfig().getDouble("game.worldBorder.default_size");
        reduceDistance = gameConfig.getConfig().getDouble("game.worldBorder.reduce_distance");
        reduceCount = gameConfig.getConfig().getInt("game.worldBorder.reduce_count");
        reduceDuration = gameConfig.getConfig().getLong("game.worldBorder.reduce_duration");
        bar = timerBar;

        worldBorder = Bukkit.createWorldBorder();
        worldBorder.setCenter(center);
        worldBorder.setSize(defaultSize);

    }

    public void run() {
        if (bar.getBar() == null) {
            return;
        }
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            int index = 1;

            @Override
            public void run() {
                float check = (float) index / reduceCount;

                if (check >= 1) {
                    worldBorder.reset();
                    Bukkit.getScheduler().cancelTask(taskId);
                }

                if (bar.getBar().getProgress() > check) {
                    worldBorder.setSize(worldBorder.getSize() - reduceDistance, reduceDuration);
                }

                index++;
            }
        }, 0, 2);

    }
}
