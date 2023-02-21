package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.PersonalVector;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiamondAbility implements MaterialAbility {

    private double damage;
    private int cooltime;
    private double length;
    GamePlayer player;
    ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");


    public DiamondAbility(GamePlayer gamePlayer) {
        this.damage = skillConfig.getConfig().getDouble("diamond.damage");
        this.length = skillConfig.getConfig().getDouble("diamond.length");
        this.cooltime = skillConfig.getConfig().getInt("diamond.cooltime");
        player = gamePlayer;
    }

    @Override
    public void activate(int cost) {
        Player user = player.getPlayer();
        Location location = user.getLocation();
        Location eyeLocation = user.getEyeLocation();
        World w = location.getWorld();

        if (user.hasCooldown(user.getInventory().getItemInMainHand().getType())) {
            int display_cooltime = (int) user.getCooldown(user.getInventory().getItemInMainHand().getType()) / 20;
            user.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        List<UUID> damagedEntityList = new ArrayList<>();   
        for (int i=0; i < length*5; i++) {
            Vector direction = PersonalVector.rotateFunction(new Vector(0.0, 0.0, (double) i/2), eyeLocation);
            Location directionLoc = new Location(user.getWorld(), eyeLocation.getX() + direction.getX(), eyeLocation.getY() + direction.getY(), eyeLocation.getZ() + direction.getZ());

            if ((i%10) == 0 && i != 0) {
                for (double index = 0; index < Math.toRadians(360); index = index + Math.toRadians(1)) {
                    Vector circle = PersonalVector.rotateFunction(
                            new Vector((2*Math.cos(index)) / 8, (2*Math.sin(index)) / 8, (double) i/2),
                            eyeLocation);
                    Location circleLoc = new Location(user.getWorld(), eyeLocation.getX() + circle.getX(), eyeLocation.getY() + circle.getY(), eyeLocation.getZ() + circle.getZ());

                    w.spawnParticle(Particle.REDSTONE, circleLoc, 1, new Particle.DustOptions(Color.fromRGB(185,242,255), 0.2f));
                }
            }

            w.spawnParticle(Particle.REDSTONE, directionLoc, 1, new Particle.DustOptions(Color.fromRGB(185,242,255), 0.7f));

            takeDamage(directionLoc, damagedEntityList, cost, damage, user);

        }

        user.setCooldown(user.getInventory().getItemInMainHand().getType(), cooltime * 20);
    }

    private void takeDamage(Location loc, List<UUID> uuids, int cost, double damage, Player user) {

        for (Entity e : loc.getWorld().getNearbyEntities(loc, 0.1, 0.1, 0.1)) {
            if (!(e instanceof LivingEntity))
                continue;
            if (e.getUniqueId().equals(user.getUniqueId()))
                continue;
            if (uuids.contains(e.getUniqueId()))
                continue;

            double d = damage * cost;

            if (Math.abs(loc.getY() - e.getLocation().getY()) >= 1.75) {
                user.sendMessage(ChatColor.RED + "**헤드샷**");
                d *= 2;
            }


            ((LivingEntity) e).damage(d, user);
            uuids.add(e.getUniqueId());

        }
    }

}
