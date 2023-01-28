package com.aren.skills;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GoldSkill implements MaterialSkills {

    private long cooldown;
    private float cooltime;
    private double duration;

    public GoldSkill(float cooltime, double duration) {
        this.cooltime = cooltime;
        this.duration = duration;
    }

    @Override
    public void activate(Player user) {

        if (cooldown > System.currentTimeMillis()) {
            double display_cooldown = (cooldown - System.currentTimeMillis()) / 1000;
            user.sendMessage("스킬을 사용하기까지 " + display_cooldown + "초 남았습니다.");
            return;
        }

        clearAll_debuffPotionEffect(user);
        cooldown = System.currentTimeMillis() + (int)(cooltime * 1000);
    }

    @Override
    public Material getCost() {
        return Material.GOLD_INGOT;
    }

    @Override
    public float getCooltime() {
        return cooltime;
    }

    @Override
    public double getDuration() {
        return duration;
    }

    public void clearAll_debuffPotionEffect(Player player) {
        List<PotionEffect> potionEffectList = new ArrayList<>();

        for (PotionEffectType type : getAllDebuffEffectType()) {
            potionEffectList.add(new PotionEffect(type, 0, 255, true, false));
        }

        player.addPotionEffects(potionEffectList);
    }

    public Collection<PotionEffectType> getAllDebuffEffectType() {
        PotionEffectType[] potionEffectTypes = new PotionEffectType[] {
                PotionEffectType.CONFUSION, PotionEffectType.DARKNESS, PotionEffectType.BLINDNESS,
                PotionEffectType.WITHER, PotionEffectType.POISON, PotionEffectType.LEVITATION,
                PotionEffectType.HUNGER, PotionEffectType.WEAKNESS, PotionEffectType.UNLUCK,
                PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING
        };
        return new ArrayList<PotionEffectType>(Arrays.asList(potionEffectTypes));
    }
}
