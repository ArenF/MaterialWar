package com.aren.utils;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;

public class WorldBarrier {

    private ConfigFile gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");

    private int taskId = -1;
    private double damage;
    private double buffer;
    private double defaultSize;
    private double reduceDistance;
    private int reduceCount;
    private long reduceDuration;
    private Location center;
    TimerBar bar;
    WorldBorder worldBorder;

    public WorldBarrier(Location center) {
        damage = gameConfig.getConfig().getDouble("game.worldBorder.damage");
        buffer = gameConfig.getConfig().getDouble("game.worldBorder.buffer");
        defaultSize = gameConfig.getConfig().getDouble("game.worldBorder.default_size");
        reduceDistance = gameConfig.getConfig().getDouble("game.worldBorder.reduce_distance");
        reduceCount = gameConfig.getConfig().getInt("game.worldBorder.reduce_count");
        reduceDuration = gameConfig.getConfig().getLong("game.worldBorder.reduce_duration");
        this.center = center;
    }

    public void setTimer(TimerBar bar) {
        this.bar = bar;
    }

    public TimerBar getTimer() {
        return bar;
    }

    public void stop() {
        worldBorder.reset();
        if (taskId != -1)
            return;
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public void run() {
        worldBorder = center.getWorld().getWorldBorder();
        worldBorder.setCenter(center);
        worldBorder.setSize(defaultSize);
        if (bar == null) {
            return;
        }
        if (gameConfig.getConfig().getBoolean("game.worldBorder.move_enable")) {
            return;
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            int index = reduceCount;

            @Override
            public void run() {
                float check = (float) index / reduceCount;

                if (check <= 0) {
                    if (worldBorder.getSize() <= 2) {
                        Bukkit.getScheduler().cancelTask(taskId);
                    } else {
                        worldBorder.setSize(worldBorder.getSize() - reduceDistance, reduceDuration / 2);
                    }

                    return;
                }

                if (bar.getBar().getProgress() < check) {
                    for (Player player : bar.getBar().getPlayers()) {
                        player.sendMessage("자기장의 위치가 줄어듭니다.");
                        player.sendMessage(bar.getBar().getProgress() + ", " + check);
                    }
                    worldBorder.setSize(worldBorder.getSize() - reduceDistance, reduceDuration);

                    index = index - 1;
                }
            }
        }, 0, 2);

    }
}
