package com.aren.skills;

import com.aren.utils.ConfigManager;
import com.aren.utils.DamageTeritory;
import com.aren.utils.PersonalCoordinates;
import com.aren.utils.data.ConfigFile;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.UUID;

public class DiamondSkill implements MaterialAbility {
//    게임 시작 시 모든 플레이어에게 능력을 HashMap으로 제공 위의 클래스로 스킬의 모든 걸 관리함
//    쿨타임 관리는 여전히 주어진 과제이다.
//    실시간 시간을 통해 쿨다운을 설정할 수 있으므로

    private final AbilityType type = AbilityType.DIAMOND;
    private ConfigFile configFile;
    private long cooldown;
    private double cooltime;
    private double damage;

    private int taskID = -1;

    public DiamondSkill() {
        configFile = ConfigManager.getInstance().getConfig("skillConfig");
    }

//    activate 함수만 만들어서, 나머지는 skillManager에게 처리할 것
    @Override
    public void activate(Player user, int cost) {
        Location location = user.getLocation();
        Location eyeLocation = user.getEyeLocation();
        World w = location.getWorld();

        if (cooldown > System.currentTimeMillis()) {
            int display_cooltime = (int) (cooldown - System.currentTimeMillis()) / 1000;
            user.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        for (int i=0; i < 50; i++) {
            Vector direction = PersonalCoordinates.rotateFunction(new Vector(0.0, 0.0, (double) i/2), eyeLocation);
            Location directionLoc = new Location(user.getWorld(), eyeLocation.getX() + direction.getX(), eyeLocation.getY() + direction.getY(), eyeLocation.getZ() + direction.getZ());

            if ((i%10) == 0 && i != 0) {
                for (double index = 0; index < Math.toRadians(360); index = index + Math.toRadians(1)) {
                    Vector circle = PersonalCoordinates.rotateFunction(
                            new Vector((2*Math.cos(index)) / 8, (2*Math.sin(index)) / 8, (double) i/2),
                            eyeLocation);
                    Location circleLoc = new Location(user.getWorld(), eyeLocation.getX() + circle.getX(), eyeLocation.getY() + circle.getY(), eyeLocation.getZ() + circle.getZ());

                    w.spawnParticle(Particle.REDSTONE, circleLoc, 1, new Particle.DustOptions(Color.fromRGB(185,242,255), 0.2f));
                }
            }

            w.spawnParticle(Particle.REDSTONE, directionLoc, 1, new Particle.DustOptions(Color.fromRGB(185,242,255), 0.7f));

            for (Entity entity : DamageTeritory.nearbyEntity(directionLoc, 1.5)) {
                if (!(entity instanceof LivingEntity))
                    continue;
                if (entity == user)
                    continue;
                LivingEntity enemy = (LivingEntity) entity;

//                double로 설정한 것은 데미지와 합산하기 편하게 하기 위해
                enemy.damage(damage * cost, user);
            }
        }

        cooldown = System.currentTimeMillis() + (int)(cooltime * 1000);
    }

    @Override
    public void load() {

    }

    @Override
    public AbilityType getAbilityType() {
        return null;
    }
}
