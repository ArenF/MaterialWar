package com.aren.skills;

import com.aren.utils.data.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GoldSkill implements MaterialSkills {

    private AbilityType type = AbilityType.GOLD_INGOT;
    private long cooldown;
    private double cooltime;
    private int maxCost;
    private double heal;


    public GoldSkill() {

    }

    @Override
    public void activate(Player user) {

        if (cooldown > System.currentTimeMillis()) {
            double display_cooldown = (cooldown - System.currentTimeMillis()) / 1000;
            user.sendMessage("스킬을 사용하기까지 " + display_cooldown + "초 남았습니다.");
            return;
        }

        World w = user.getWorld();
        w.spawnParticle(Particle.COMPOSTER, user.getLocation(), 35, 1.5, 1.5, 1.5, true);

        int cost = getCostToHeal(user.getInventory());
        double healing = cost*heal > user.getHealthScale() ? user.getHealthScale() : cost*heal;

        user.setHealth(healing);

        clearAll_debuffPotionEffect(user);
        cooldown = System.currentTimeMillis() + (int)(cooltime * 1000);
    }

    public int getCostToHeal(PlayerInventory inventory) {
        int amount = inventory.getItemInMainHand().getAmount();
        int cost = 0;

        if (maxCost >= amount) {
            cost = amount;
        } else {
            cost = maxCost;
        }

        if (amount > cost) {
            inventory.getItemInMainHand().setAmount(amount - cost);
        } else if (amount == cost) {
            inventory.removeItem(inventory.getItemInMainHand());
        }
        return cost;
    }


    @Override
    public AbilityType getType() {
        return type;
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
