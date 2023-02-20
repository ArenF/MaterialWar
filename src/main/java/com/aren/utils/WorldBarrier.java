package com.aren.utils;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;

public class WorldBarrier {

    private ConfigFile gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");

    private double damage;
    private double buffer;
    private double defaultSize;
    private double reduceDistance;

    public WorldBarrier(Location center) {
        WorldBorder worldborder = Bukkit.createWorldBorder();
        worldborder.setCenter(center);

        damage = gameConfig.getConfig().getDouble("game.worldBorder.damage");
        buffer = gameConfig.getConfig().getDouble("game.worldBorder.buffer");
        defaultSize = gameConfig.getConfig().getDouble("game.worldBorder.default_size");
        reduceDistance = gameConfig.getConfig().getDouble("game.worldBorder.reduce_distance");
    }
}
