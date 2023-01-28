package com.aren.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface MaterialSkills {
    void activate(Player user);
    Material getCost();

    float getCooltime();
    double getDuration();
}