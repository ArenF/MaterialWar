package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.player.GamePlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LapisAbility implements MaterialAbility {

    private ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    GamePlayer player;

    private int gainBuffInterval;
    private int cooltime;
    private int duration;
    private int levelDivided;
    private int taskId = -1;

    public LapisAbility(GamePlayer player) {
        this.player = player;
        gainBuffInterval = skillConfig.getConfig().getInt("lapis.gainBuffInterval");
        duration = skillConfig.getConfig().getInt("lapis.duration");
        levelDivided = skillConfig.getConfig().getInt("lapis.level_divided");
        cooltime = skillConfig.getConfig().getInt("lapis.cooltime");
    }

    @Override
    public void activate(int cost) {
        Player p = player.getPlayer();
        List<PotionEffectType> buffs = getBuffers();

        if (taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
        }
        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.5f, 0.01f);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            World w = p.getWorld();
            int count = 0;
            @Override
            public void run() {

                if (count >= 20) {
                    w.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                    Random rand = new Random();

                    int count = cost / gainBuffInterval == 0 ? 1 : cost / gainBuffInterval;
                    int level = cost / levelDivided;

                    for (int i = 0; i < count; i++) {
                        int r = rand.nextInt(buffs.size());
                        p.addPotionEffect(new PotionEffect(buffs.get(r), duration*20, level, true, false, false));
                        p.sendMessage("효과 : " + buffs.get(r).getName());
                        p.sendMessage("레벨 : " + level);
                    }

                    Bukkit.getScheduler().cancelTask(taskId);
                    taskId = -1;
                }

                for (double round = 0; round < 2*Math.PI; round += Math.PI/5) {
                    Vector vector = new Vector(2*Math.cos(round), 0.1, 2*Math.sin(round));

                    w.spawnParticle(Particle.ENCHANTMENT_TABLE, p.getEyeLocation(), 0, vector.getX(), vector.getY(), vector.getZ(), 1, null, true);
                }

                count++;
            }
        }, 0, 2);
    }

    private List<PotionEffectType> getBuffers() {
        List<PotionEffectType> result = new ArrayList<>();
        result.add(PotionEffectType.LUCK); result.add(PotionEffectType.SPEED);
        result.add(PotionEffectType.JUMP); result.add(PotionEffectType.NIGHT_VISION);
        result.add(PotionEffectType.INCREASE_DAMAGE); result.add(PotionEffectType.SLOW_FALLING);
        result.add(PotionEffectType.REGENERATION); result.add(PotionEffectType.SLOW);
        result.add(PotionEffectType.SATURATION); result.add(PotionEffectType.GLOWING);
        result.add(PotionEffectType.FIRE_RESISTANCE); result.add(PotionEffectType.INVISIBILITY);

        return result;
    }

    @Override
    public void load() {
        skillConfig.load();
        gainBuffInterval = skillConfig.getConfig().getInt("lapis.gainBuffInterval");
        duration = skillConfig.getConfig().getInt("lapis.duration");
        levelDivided = skillConfig.getConfig().getInt("lapis.level_divided");
        cooltime = skillConfig.getConfig().getInt("lapis.cooltime");
    }
}
