package com.aren.utils.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameConfig implements ConfigFile {

    private String name = "GameConfig";
    private File file;
    private FileConfiguration config;

    public GameConfig(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder() + "/" + name + ".yml");
        createFile();
    }

    public void createFile() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public FileConfiguration getConfiguration() {
        return config;
    }

    @Override
    public Object get(String name) {
        load();
        return config.get(name);
    }

    @Override
    public void set(String name, Object value) {
        config.set(name, value);
        save();
    }

    @Override
    public void createDataSection() {
        load();

        if (!config.getKeys(false).isEmpty())
            return;

        config.set("game.invincible_duration", 0);
        config.set("game.invincible_color", "RED");
        config.set("game.time", 120);
        config.set("game.time_color", "RED");
        config.set("game.default_item", null);
        save();
    }

    @Override
    public void load() {
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
