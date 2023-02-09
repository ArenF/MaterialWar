package com.aren.utils;

import com.aren.skills.AbilityType;
import com.aren.utils.data.ConfigFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ConfigManager {
//    초기 스킬 설정을 도와주는 객체
//    스킬 관련 콘피그 파일을 생성하고 해당 파일의 데이터를 전달
//    스킬 관련 값을 설정하고 저장하고 불러와 제대로된 스킬을 불러온다.

    private static ConfigManager instance;

    HashMap<String, ConfigFile> configs = new HashMap<String, ConfigFile>();

    public ConfigManager() {}

    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }

        return instance;
    }

    public void createConfigFile(String name, ConfigFile configFile) {
        configs.put(name, configFile);
    }

    public void configDataSet() {
        for (ConfigFile configFile : configs.values()) {
            configFile.createDataSection();
        }
    }

    public ConfigFile getConfig(String name) {
        return configs.get(name);
    }

}
