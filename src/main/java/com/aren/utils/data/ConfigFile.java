package com.aren.utils.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigFile {

    File file;
    FileConfiguration config;

    public ConfigFile(String name, String path) {
        this.file = new File("plugins/" + path, name + ".yml");

        createFile();
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public boolean isExist() {
        return file.exists();
    }

    public boolean isEmpty() {
        return config.getKeys(false).isEmpty();
    }

    public void createFile() {
        if (!(file.exists())) {
            file.mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("문제가 발생하였습니다. plugins 파일에 오류가 발생하였는지 확인해주시기 바랍니다.");
            }
        }
    }

    public Object get(String key) {
        try {
            config.load(file);
            return config.get(key);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String key, Object value) {
        FileConfiguration config = this.config;

        try {
            config.set(key, value);
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return file;
    }
}
