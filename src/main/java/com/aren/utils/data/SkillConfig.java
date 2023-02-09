package com.aren.utils.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SkillConfig implements ConfigFile {

    private String name = "SkillConfig";
    private File file;
    private FileConfiguration config;

    public SkillConfig(JavaPlugin plugin) {
        file = new File(plugin.getDataFolder() + "/" + name + ".yml");
        createFile();

    }

    private void createFile() {
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
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        return config.get(name);
    }

    @Override
    public void set(String name, Object value) {
        config.set(name, value);
        save();
    }

    @Override
    public void createDataSection() {
        if (config == null) {
            load();
        }

        if (!config.getKeys(false).isEmpty())
            return;

        config.set("diamond.damage", 2);
        config.set("diamond.length", 10);

        config.set("gold_ingot.heal", 2);
        config.set("gold_ingot.condition_count", 5);

        config.set("iron_ingot.spear.length", 3);
        config.set("iron_ingot.spear.damage", 6);
        config.set("iron_ingot.sword.damage", 5);
        config.set("iron_ingot.sword.amplitude", 4);
        config.set("iron_ingot.sword.limit", 8);
        config.set("iron_ingot.dagger.length", 0.5);
        config.set("iron_ingot.dagger.damage", 9);
        config.set("iron_ingot.duration", 5);

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
