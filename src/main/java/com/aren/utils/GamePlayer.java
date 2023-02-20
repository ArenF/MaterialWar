package com.aren.utils;

import com.aren.ability.AbilityType;
import com.aren.ability.MaterialAbility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayer {

    private String name;
    private UUID uniqueId;
    private HashMap<AbilityType, MaterialAbility> abilities = new HashMap<>();

    public GamePlayer(Player player) {
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
    }

    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void addAbility(AbilityType type, MaterialAbility ability) {
        abilities.put(type, ability);
    }

    public void setAbilities(HashMap<AbilityType, MaterialAbility> abilities) {
        this.abilities = abilities;
    }

    public HashMap<AbilityType, MaterialAbility> getAbilities() {
        return abilities;
    }

    public MaterialAbility getAbility(AbilityType type) {
        return abilities.get(type);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uniqueId);
    }
}
