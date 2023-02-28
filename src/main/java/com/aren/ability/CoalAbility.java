package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.PersonalVector;
import com.aren.utils.player.GamePlayer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CoalAbility implements MaterialAbility {

    ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    GamePlayer player;

    private int cooltime;
    private double velocity;
    private double damage;
    private double deletedDistance;

    private int taskId = -1;

    public CoalAbility(GamePlayer player) {
        this.player = player;
        cooltime = skillConfig.getConfig().getInt("coal.cooltime");
        velocity = skillConfig.getConfig().getDouble("coal.velocity");
        damage = skillConfig.getConfig().getDouble("coal.damage");
        deletedDistance = skillConfig.getConfig().getDouble("coal.deletedDistance");
    }

    @Override
    public void activate(int cost) {
        Player p = player.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        List<Fireball> fireballList = new ArrayList<>();

        if (p.hasCooldown(item.getType())) {
            int display_cooltime = (int) p.getCooldown(p.getInventory().getItemInMainHand().getType()) / 20;
            p.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1.0f);

        Random rand = new Random();
        for (int i = 0; i < cost; i++) {
            Vector vector;
            if (i == 0) {
                vector = new Vector(0, 0, velocity);
            } else {
                vector = new Vector(rand.nextDouble(1) - 0.5, rand.nextDouble(0.5) - 0.25, velocity);
            }
            Fireball fb = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.SMALL_FIREBALL);

            fb.setVelocity(PersonalVector.rotateFunction(vector, p.getEyeLocation()));
            fb.setGravity(true);
            fb.setDirection(p.getEyeLocation().getDirection());

            fireballList.add(fb);
        }
        if (taskId != -1) {
            MaterialWar.getPlugin().getLogger().info("해당 스킬의 스케줄러가 사라졌습니다.");
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            List<Fireball> fbList = new ArrayList<>(fireballList);
            Location loc = p.getLocation();
            World w = p.getWorld();
            List<UUID> uuidList = new ArrayList<>();

            @Override
            public void run() {
                int endScore = 0;
                for (Fireball fb : fbList) {
                   if (fb.getLocation().distance(loc) > deletedDistance) {
                       endScore++;
                       w.spawnParticle(Particle.SMOKE_NORMAL, fb.getLocation(), 5, 0, 0, 0, 0, null, true);
                       fb.remove();
                       continue;
                   }
                   if (fb.isDead()) {
                       endScore++;
                       continue;
                   }
                    for (Entity e : w.getNearbyEntities(fb.getLocation(), 1, 1, 1)) {
                        if (!(e instanceof LivingEntity))
                            continue;
                        if (uuidList.contains(e.getUniqueId()))
                            continue;
                        if (e == p)
                            continue;

                        w.spawnParticle(Particle.LAVA, fb.getLocation(), 16, 0, 0, 0, 0, null, true);
                        ((LivingEntity) e).damage(damage * (double)(cost / 10), p);
                        uuidList.add(e.getUniqueId());
                    }
                }

                if (endScore == fbList.size()) {
                    MaterialWar.getPlugin().getLogger().info("해당 스킬의 스케줄러가 종료되었습니다.");
                    Bukkit.getScheduler().cancelTask(taskId);
                }
            }
        }, 0, 2);

        p.setCooldown(item.getType(), cooltime * 20);
        p.getInventory().removeItem(item);
    }

    @Override
    public void load() {
        skillConfig.load();
        cooltime = skillConfig.getConfig().getInt("coal.cooltime");
        velocity = skillConfig.getConfig().getDouble("coal.velocity");
        damage = skillConfig.getConfig().getDouble("coal.damage");
    }
}
