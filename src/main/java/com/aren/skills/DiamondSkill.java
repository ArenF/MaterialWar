package com.aren.skills;

import com.aren.utils.DamageTeritory;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class DiamondSkill implements MaterialSkills {
//    게임 시작 시 모든 플레이어에게 능력을 HashMap으로 제공 위의 클래스로 스킬의 모든 걸 관리함
//    쿨타임 관리는 여전히 주어진 과제이다.
//    실시간 시간을 통해 쿨다운을 설정할 수 있으므로

    private long cooldown = 0;
    private float cooltime = 0.5f;
    private double damage;

    public DiamondSkill(float cooltime, double damage) {
        this.cooltime = cooltime;
        this.damage = damage;
    }
    @Override
    public void activate(Player user) {
        Location location = user.getLocation();
        World w = location.getWorld();
        Vector vector = user.getLocation().getDirection();

        if (cooldown > System.currentTimeMillis()) {
            int display_cooltime = (int) (cooldown - System.currentTimeMillis()) / 1000;
            user.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        for (int i=0; i < 50; i++) {
            vector = vector.add(new Vector(0, 0, i/5));
            Location loc = user.getLocation().add(vector);
            if ((i%5) == 0) {
                for (int index=0;index<360;index++) {
                    w.spawnParticle(Particle.REDSTONE, loc.getX() + 2 * Math.cos(index), loc.getY() + 2 * Math.sin(index), loc.getZ(),
                            1, new Particle.DustOptions(Color.fromRGB(185,242,255), 1));
                }
            }

            w.spawnParticle(Particle.REDSTONE, loc, 1, new Particle.DustOptions(Color.fromRGB(185,242,255), 0.5f));

            for (Entity entity : DamageTeritory.nearbyEntity(loc, 1.5)) {
                if (!(entity instanceof LivingEntity))
                    return;
                if (entity == user)
                    return;

                ((LivingEntity) entity).damage(damage, user);
            }
        }

        cooldown = System.currentTimeMillis() + (int)(cooltime * 1000);
    }

    @Override
    public Material getCost() {
        return Material.DIAMOND;
    }

    @Override
    public float getCooltime() {
        return cooltime;
    }

    @Override
    public double getDuration() {
        return damage;
    }


    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }
}
