package com.aren.skills;

import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IronSkill implements MaterialAbility {

    private AbilityType abilityType =  AbilityType.IRON_INGOT;
    private long cooldown;
    private float cooltime = 0;
    private double duration;

    public IronSkill() {

    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return this.duration;
    }


    @Override
    public void activate(Player user, int cost) {

    }

    @Override
    public void load() {

    }

    @Override
    public AbilityType getAbilityType() {
        return abilityType;
    }

}