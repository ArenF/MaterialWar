package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.utils.player.GamePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class GoldAbility implements MaterialAbility {

    private GamePlayer player;
    private double heal;
    private int cooltime;
    private int removeEffectLimit;

    ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    public GoldAbility(GamePlayer player) {
        this.player = player;
        heal = skillConfig.getConfig().getDouble("gold.heal");
        cooltime = skillConfig.getConfig().getInt("gold.cooltime");
        removeEffectLimit = skillConfig.getConfig().getInt("gold.removeEffectCost");
    }

    @Override
    public void load() {
        skillConfig.load();
        heal = skillConfig.getConfig().getDouble("gold.heal");
        cooltime = skillConfig.getConfig().getInt("gold.cooltime");
        removeEffectLimit = skillConfig.getConfig().getInt("gold.removeEffectCost");
    }

    @Override
    public void activate(int cost) {
        Player user = player.getPlayer();
        World w = user.getWorld();

        if (user.hasCooldown(user.getInventory().getItemInMainHand().getType())) {
            int display_cooltime = (int) user.getCooldown(user.getInventory().getItemInMainHand().getType()) / 20;
            user.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }

        if (cost > removeEffectLimit) {
            for (PotionEffectType type : debufftypes()) {
                user.removePotionEffect(type);
            }
            user.setFireTicks(0);
        }

        double addHealth = heal * cost;

        w.playSound(user.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2f, 1.0f);
        w.spawnParticle(Particle.END_ROD, user.getLocation().getX(), user.getLocation().getY() + 1,
                user.getLocation().getZ(), 16, 0.2, 0.5, 0.2, 0, null, false);
        user.setHealth(Math.min(user.getHealth() + addHealth, user.getHealthScale()));

        user.setCooldown(user.getInventory().getItemInMainHand().getType(), cooltime * 20);
        user.getInventory().removeItem(user.getInventory().getItemInMainHand());
    }

    private List<PotionEffectType> debufftypes() {
        List<PotionEffectType> result = new ArrayList<>();
        result.add(PotionEffectType.BLINDNESS); result.add(PotionEffectType.GLOWING);
        result.add(PotionEffectType.BAD_OMEN); result.add(PotionEffectType.CONFUSION);
        result.add(PotionEffectType.DARKNESS); result.add(PotionEffectType.HUNGER);
        result.add(PotionEffectType.HARM); result.add(PotionEffectType.WITHER);
        result.add(PotionEffectType.POISON); result.add(PotionEffectType.SLOW);
        result.add(PotionEffectType.SLOW_DIGGING); result.add(PotionEffectType.SLOW_FALLING);
        result.add(PotionEffectType.UNLUCK); result.add(PotionEffectType.WEAKNESS);
        result.add(PotionEffectType.LEVITATION);

        return result;
    }
}
