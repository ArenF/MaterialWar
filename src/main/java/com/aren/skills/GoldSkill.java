package com.aren.skills;

import com.aren.utils.ConfigManager;
import com.aren.utils.data.ConfigFile;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GoldSkill implements MaterialAbility {

    private AbilityType abilityType = AbilityType.GOLD_INGOT;
    private long cooldown;
    private double cooltime;
    private double heal;
    private ConfigFile configFile;


    public GoldSkill() {
        configFile = ConfigManager.getInstance().getConfig("SkillConfig");
        cooltime = Double.parseDouble((String) configFile.get("gold_ingot.cooltime"));
        heal = Double.parseDouble((String) configFile.get("gold_ingot.heal"));

    }

    @Override
    public void activate(Player user, int cost) {

        if (cooldown > System.currentTimeMillis()) {
            double display_cooldown = (cooldown - System.currentTimeMillis()) / 1000;
            user.sendMessage("스킬을 사용하기까지 " + display_cooldown + "초 남았습니다.");
            return;
        }

        World w = user.getWorld();
        w.spawnParticle(Particle.COMPOSTER, user.getLocation(), 35, 1.5, 1.5, 1.5, true);

        double healing = Math.min(cost * heal, user.getHealthScale());

        user.setHealth(healing);

        clearAll_debuffPotionEffect(user);
        cooldown = System.currentTimeMillis() + (int)(cooltime * 1000);
    }

    @Override
    public void load() {

    }

    @Override
    public AbilityType getAbilityType() {
        return abilityType;
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
