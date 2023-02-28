package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.player.GamePlayer;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedstoneAbility implements MaterialAbility {

    private ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");
    GamePlayer player;

    private double range;
    private double damage;
    private int duration;
    private int costInterval;
    private int slowness;
    private int cooltime;
    private int taskId = -1;

    public RedstoneAbility(GamePlayer player) {
        this.player = player;
        range = skillConfig.getConfig().getDouble("redstone.range");
        costInterval = skillConfig.getConfig().getInt("redstone.cost_interval");
        damage = skillConfig.getConfig().getDouble("redstone.damage");
        duration = skillConfig.getConfig().getInt("redstone.duration");
        slowness = skillConfig.getConfig().getInt("redstone.slowness");
        cooltime = skillConfig.getConfig().getInt("redstone.cooltime");
    }

    @Override
    public void activate(int cost) {
        Player p = player.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        World w = p.getWorld();
        Location loc = p.getLocation();
        int interval = (int)cost / costInterval < 1 ? 1 : cost / costInterval;
        List<UUID> uuidList = new ArrayList<>();

        if (p.hasCooldown(item.getType())) {
            int display_cooltime = (int) p.getCooldown(p.getInventory().getItemInMainHand().getType()) / 20;
            p.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        for (double i = 0; i < Math.PI; i += Math.PI/(20 + cost)) {
            for (double theta = 0; theta <= 2*Math.PI; theta += Math.PI / 10) {
                double x = (range * interval) * Math.cos(theta)*Math.sin(i);
                double y = (range * interval) * Math.cos(i) + 1.5;
                double z = (range * interval) * Math.sin(theta)*Math.sin(i);

                loc.add(x, y, z);

                w.spawnParticle(Particle.REDSTONE, loc, 1 * interval, 0.5, 0.5, 0.5, 0, new Particle.DustOptions(Color.fromRGB(228,0,70), 1.0f));

                loc.subtract(x, y, z);
            }
        }

        for (Entity e : w.getNearbyEntities(loc, (range*interval), (range*interval), (range*interval))) {
            if (!(e instanceof LivingEntity))
                continue;
            if (uuidList.contains(e.getUniqueId()))
                continue;
            if (e == p)
                continue;

            ((LivingEntity) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, slowness, true, false, false));
            uuidList.add(e.getUniqueId());
        }

        List<LivingEntity> employees = new ArrayList<>();
        for (UUID uuid : uuidList) {
            Entity e = Bukkit.getEntity(uuid);
            if (!(e instanceof LivingEntity))
                continue;
            if (e.isDead())
                continue;
            employees.add((LivingEntity) e);
            double distance = ((LivingEntity) e).getEyeLocation().distance(p.getEyeLocation());
            p.sendMessage(e.getType() + ", " + distance);
        }

        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        w.playSound(loc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 0.5f, 1.0f);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            int taken = 0;

            @Override
            public void run() {
                if (taken >= 80) {
                    if (!employees.isEmpty()) {
                        double damaged = damage * Math.round(cost/costInterval);
                        for (LivingEntity e : employees) {
//                        w.spawnParticle(Particle.BLOCK_CRACK);
                            e.damage(damaged, p);
                            p.setHealth(Math.min(p.getHealth() + damaged/4, p.getHealthScale()));
                        }
                        w.playSound(p.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, 0.1f, 1.0f);
                    }

                    Bukkit.getScheduler().cancelTask(taskId);
                    taskId = -1;
                }

                for (LivingEntity e : employees) {
                    Vector vec = new Vector(e.getEyeLocation().getX() - p.getEyeLocation().getX(),
                            e.getEyeLocation().getY() - p.getEyeLocation().getY(),
                            e.getEyeLocation().getZ() - p.getEyeLocation().getZ());
                    double distance = e.getEyeLocation().distance(p.getEyeLocation());

                    Location particleLocation = p.getEyeLocation().subtract(0, 0.5, 0);
                    for (double i = 1; i < distance; i += 0.25) {
                        vec.multiply(i);
                        particleLocation.add(vec);
                        w.spawnParticle(Particle.REDSTONE, particleLocation, 1, 0, 0, 0, 0, new Particle.DustOptions(Color.fromRGB(228,0,70), 0.5f), true);
                        particleLocation.subtract(vec);
                        vec = vec.normalize();
                    }

                }
                taken++;
            }
        }, 0, 2);
        p.setCooldown(item.getType(), cooltime * 20);
        p.getInventory().removeItem(item);
    }

    @Override
    public void load() {
        skillConfig.load();
        range = skillConfig.getConfig().getDouble("redstone.range");
        costInterval = skillConfig.getConfig().getInt("redstone.cost_interval");
        damage = skillConfig.getConfig().getDouble("redstone.damage");
        duration = skillConfig.getConfig().getInt("redstone.duration");
        slowness = skillConfig.getConfig().getInt("redstone.slowness");
        cooltime = skillConfig.getConfig().getInt("redstone.cooltime");
    }
}
