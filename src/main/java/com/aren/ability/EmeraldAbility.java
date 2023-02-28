package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EmeraldAbility implements MaterialAbility {

    private ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");
    GamePlayer player;

    private int duration;
    private int cooltime;

    private int taskId = -1;

    public EmeraldAbility(GamePlayer player) {
        this.player = player;
        duration = skillConfig.getConfig().getInt("emerald.duration");
        cooltime = skillConfig.getConfig().getInt("emerald.cooltime");
    }

    @Override
    public void activate(int cost) {
        Player p = player.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();

        if (p.hasCooldown(item.getType())) {
            int display_cooltime = (int) p.getCooldown(p.getInventory().getItemInMainHand().getType()) / 20;
            p.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration * cost * 20, 1, true, false, false));
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            World w = p.getWorld();
            int durate = duration * cost * 10;

            @Override
            public void run() {
                if (durate <= 0) {
                    Bukkit.getScheduler().cancelTask(taskId);
                }

                if (durate % 10 == 0) {
                    w.playSound(p.getLocation(), Sound.ITEM_TRIDENT_RETURN, 1.0f, 1.0f);
                    w.spawnParticle(Particle.COMPOSTER, p.getEyeLocation(), 50, 0.5, 0.5, 0.5, 0, null, true);
                    w.spawnParticle(Particle.END_ROD, p.getEyeLocation(), 50, 0.5, 0.5, 0.5, 0, null, true);
                }

                if (p.getHealth() < p.getHealthScale())
                    p.setHealth(p.getHealthScale());
                durate--;
            }
        }, 0, 2);

        p.setCooldown(p.getInventory().getItemInMainHand().getType(), cooltime * cost * 20);
        p.getInventory().removeItem(p.getInventory().getItemInMainHand());
    }

    @Override
    public void load() {
        skillConfig.load();
        duration = skillConfig.getConfig().getInt("emerald.duration");
        cooltime = skillConfig.getConfig().getInt("emerald.cooltime");
    }
}
