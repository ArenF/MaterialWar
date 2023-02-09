package com.aren.skills;

import com.aren.utils.data.ConfigFile;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class IronSkill implements MaterialSkills {

    private AbilityType type = AbilityType.IRON_INGOT;
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
    public void activate(Player user) {

        if (cooldown > System.currentTimeMillis()) {
            double display_cooldown = (cooldown - System.currentTimeMillis()) / 1000;
            user.sendMessage("스킬을 사용할 때까지 " + display_cooldown + "초 남았습니다.");
            return;
        }

        World w = user.getWorld();
        double x = user.getLocation().getX();
        double y = user.getLocation().getY();
        double z = user.getLocation().getZ();

        w.spawnParticle(Particle.ENCHANTMENT_TABLE, x, y, z, 35, 1, 1, 1, 0, null);

        user.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, true, false));

        cooldown = System.currentTimeMillis() + (int)(cooltime * 1000);
    }

    @Override
    public AbilityType getType() {
        return type;
    }

}