package com.aren.utils;

import com.aren.skills.IronSkill;
import com.aren.skills.MaterialSkills;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GamePlayer {

    private UUID uuid;
    private String name;
    private HashMap<String, MaterialSkills> playerSkills;

    public GamePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public HashMap<String, MaterialSkills> getPlayerSkills() {
        return playerSkills;
    }

    public void addSkill(MaterialSkills skill) {
        getPlayerSkills().put(skill.getCost().name(), skill);
    }

    public void addAllSkill(HashMap<String, MaterialSkills> map) {
        playerSkills = map;
    }
}
