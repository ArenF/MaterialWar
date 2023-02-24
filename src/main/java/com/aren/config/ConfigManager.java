package com.aren.config;

import java.util.HashMap;

public class ConfigManager {

    private static ConfigManager instance;

    private HashMap<String, ConfigFile> configFileMap = new HashMap<String, ConfigFile>();

    public ConfigManager() {

    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public void load() {
        for (ConfigFile configFile : configFileMap.values()) {
            configFile.load();
        }
    }

    public HashMap<String, ConfigFile> getConfigFiles() {
        return configFileMap;
    }

    public void addConfigFile(String name, ConfigFile configFile) {
        configFileMap.put(name, configFile);
    }

    public ConfigFile getConfigFile(String name) {
        return configFileMap.get(name);
    }
}
