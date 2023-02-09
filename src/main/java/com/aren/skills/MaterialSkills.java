package com.aren.skills;

import com.aren.utils.data.ConfigFile;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface MaterialSkills {
    void activate(Player user);
    AbilityType getType();

}