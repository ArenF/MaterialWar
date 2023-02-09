package com.aren.skills;

import org.bukkit.entity.Player;

public interface MaterialAbility {
    void activate(Player user, int cost);
    void load();
    AbilityType getAbilityType();

}