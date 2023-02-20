package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.utils.GamePlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class GoldAbility implements MaterialAbility {

    private GamePlayer player;
    private double heal;
    private int cooltime;

    ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    public GoldAbility(GamePlayer player) {
        this.player = player;
        heal = skillConfig.getConfig().getDouble("gold.heal");
        cooltime = skillConfig.getConfig().getInt("gold.cooltime");

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

        double addHealth = heal * cost;

        w.playSound(user.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2f, 1.0f);
        w.spawnParticle(Particle.END_ROD, user.getLocation().getX(), user.getLocation().getY() + 1,
                user.getLocation().getZ(), 16, 0.2, 0.5, 0.2, 0, null, false);
        user.setHealth(Math.min(user.getHealth() + addHealth, user.getHealthScale()));

        user.setCooldown(user.getInventory().getItemInMainHand().getType(), cooltime * 20);
    }
}
