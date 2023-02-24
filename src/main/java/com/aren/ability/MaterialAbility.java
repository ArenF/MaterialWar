package com.aren.ability;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public interface MaterialAbility {

    void activate(int cost);
    void load();
}
