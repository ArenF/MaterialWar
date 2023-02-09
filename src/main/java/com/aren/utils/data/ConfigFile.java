package com.aren.utils.data;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface ConfigFile {

    File getFile();
    FileConfiguration getConfiguration();

    Object get(String name);
    void set(String name, Object value);
    void save();
    void load();
    void createDataSection();
}
