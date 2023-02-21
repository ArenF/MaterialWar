package com.aren.utils.player;

import com.aren.ability.AbilityType;
import com.aren.ability.MaterialAbility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayer {

    private PlayerState state;
    private String name;
    private UUID uniqueId;
    private HashMap<AbilityType, MaterialAbility> abilities = new HashMap<>();

    public GamePlayer(Player player, PlayerState state) {
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
        this.state = state;
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
