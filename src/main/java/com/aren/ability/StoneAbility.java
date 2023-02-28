package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.PersonalVector;
import com.aren.utils.player.GamePlayer;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class StoneAbility implements MaterialAbility {

    GamePlayer player;

    private ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    private int cooltime;
    private double damage;
    private double knockback;
    private double velocity;
    private int taskId = -1;

    public StoneAbility(GamePlayer player) {
        this.player = player;
        cooltime = skillConfig.getConfig().getInt("stone.cooltime");
        damage = skillConfig.getConfig().getDouble("stone.damage");
        velocity = skillConfig.getConfig().getDouble("stone.velocity");
        knockback = skillConfig.getConfig().getDouble("stone.knockback");
    }

    @Override
    public void load() {
        skillConfig.load();
        cooltime = skillConfig.getConfig().getInt("stone.cooltime");
        damage = skillConfig.getConfig().getDouble("stone.damage");
        velocity = skillConfig.getConfig().getDouble("stone.velocity");
        knockback = skillConfig.getConfig().getDouble("stone.knockback");
    }

    @Override
    public void activate(int cost) {

        List<FallingBlock> blocks = new ArrayList<>();
        Player p = player.getPlayer();
        ItemStack item = p.getInventory().getItemInMainHand();
        if (p.hasCooldown(item.getType())) {
            int display_cooltime = (int) p.getCooldown(p.getInventory().getItemInMainHand().getType()) / 20;
            p.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }


        Random rand = new Random();
        for (int i = 0; i < cost; i++) {
            Vector vector;
            if (i == 0) {
                vector = new Vector(0, 0.2, velocity);
            } else {
                vector = new Vector(rand.nextDouble(1) - 0.5, rand.nextDouble(0.5) - 0.25, velocity);
            }
            FallingBlock b = p.getWorld().spawnFallingBlock(p.getEyeLocation(), Material.COBBLESTONE.createBlockData());

            b.setVelocity(PersonalVector.rotateFunction(vector, p.getEyeLocation()));
            b.setGravity(true);
            b.setDropItem(true);

            blocks.add(b);
        }
        if (taskId != -1) {
            MaterialWar.getPlugin().getLogger().info("해당 스킬의 스케줄러가 사라졌습니다.");
            Bukkit.getScheduler().cancelTask(taskId);
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            World w = p.getWorld();
            List<FallingBlock> blockList = new ArrayList<>(blocks);
            private List<UUID> uuidList = new ArrayList<>();
            @Override
            public void run() {
                int deadScore = 0;
                for (FallingBlock block : blockList) {
                    if (block.isDead()) {
                        deadScore++;
                        continue;
                    }
                    for (Entity e : w.getNearbyEntities(block.getLocation(), 1, 1, 1)) {
                        if (!(e instanceof LivingEntity))
                            continue;
                        if (uuidList.contains(e.getUniqueId()))
                            continue;
                        if (e == p)
                            continue;

                        w.spawnParticle(Particle.BLOCK_DUST, block.getLocation(), 32, 0, 0, 0, 0, Material.COBBLESTONE.createBlockData(), true);
                        ((LivingEntity) e).damage(damage, p);
                        e.setVelocity(block.getVelocity().multiply(knockback));
                        uuidList.add(e.getUniqueId());
                    }
                }
                if (deadScore >= blockList.size()) {
                    Bukkit.getScheduler().cancelTask(taskId);
                }
            }
        }, 0, 2);
        p.setCooldown(item.getType(), cooltime * 20);
        p.getInventory().removeItem(p.getInventory().getItemInMainHand());
    }
}
