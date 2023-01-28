package com.aren.utils;

import com.aren.skills.DiamondSkill;
import com.aren.skills.GoldSkill;
import com.aren.skills.IronSkill;
import com.aren.skills.MaterialSkills;
import com.aren.utils.data.ConfigFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class SkillManager {
//    초기 스킬 설정을 도와주는 객체
//    스킬 관련 콘피그 파일을 생성하고 해당 파일의 데이터를 전달
//    스킬 관련 값을 설정하고 저장하고 불러와 제대로된 스킬을 불러온다.

    private static SkillManager instance;

    private ConfigFile skillDataFile = new ConfigFile("MaterailSkill", "skill");
    private HashMap<String, MaterialSkills> skillMap = new HashMap<>();

    public SkillManager() {

    }

    public HashMap<String, MaterialSkills> getSkills() {
        return skillMap;
    }

    public ConfigFile getFile() {
        return skillDataFile;
    }

    public void load() {
        skillDataFile.createFile();
        FileConfiguration config = skillDataFile.getConfig();

        for (String name : config.getKeys(false)) {
            float cooltime = (float)skillDataFile.get(name + ".cooltime");
            double duration = (double) skillDataFile.get(name + ".duration");
            MaterialSkills skill = null;
            switch (name) {
                case "DIAMOND":
                    skill = new DiamondSkill(cooltime, duration);
                    break;
                case "IRON":
                    skill = new IronSkill(cooltime, duration);
                    break;
                case "GOLD":
                    skill = new GoldSkill(cooltime, duration);
                    break;
            }
            skillMap.put(name, skill);
        }
    }

    public void addNSave(MaterialSkills skill) {
        add(skill);
        save(skill.getCost().name(), skill);
    }

    public void add(MaterialSkills skill) {
        skillMap.put(skill.getCost().name(), skill);
    }

    public void save(String name, MaterialSkills ms) {
        skillDataFile.set(name + ".cooltime", ms.getCooltime());
        skillDataFile.set(name + ".duration", ms.getDuration());
    }

    public static SkillManager getInstance() {
        if (instance == null) {
            instance = new SkillManager();
        }

        return instance;
    }



}
