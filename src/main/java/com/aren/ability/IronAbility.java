package com.aren.ability;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.PersonalVector;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IronAbility implements MaterialAbility {

    private GamePlayer player;
    private int taskId = -1;
    private int cooltime;
    private int interval;
    private List<Material> ironTools = new ArrayList<>();
    private List<Enchantment> enchantable = new ArrayList<>();
    private ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    public IronAbility(GamePlayer player) {
        this.player = player;
        cooltime = skillConfig.getConfig().getInt("iron.cooltime");
        interval = skillConfig.getConfig().getInt("iron.level_interval");
        ironTools.add(Material.IRON_SWORD); ironTools.add(Material.IRON_AXE);
        ironTools.add(Material.IRON_SHOVEL); ironTools.add(Material.IRON_PICKAXE);
        ironTools.add(Material.IRON_HOE); ironTools.add(Material.IRON_HELMET);
        ironTools.add(Material.IRON_CHESTPLATE); ironTools.add(Material.IRON_LEGGINGS);
        ironTools.add(Material.IRON_BOOTS);

        enchantable.add(Enchantment.PROTECTION_FIRE); enchantable.add(Enchantment.PROTECTION_PROJECTILE);
        enchantable.add(Enchantment.PROTECTION_ENVIRONMENTAL); enchantable.add(Enchantment.PROTECTION_FALL);
        enchantable.add(Enchantment.PROTECTION_EXPLOSIONS); enchantable.add(Enchantment.DAMAGE_ALL);
        enchantable.add(Enchantment.DAMAGE_UNDEAD); enchantable.add(Enchantment.FIRE_ASPECT);
        enchantable.add(Enchantment.DURABILITY); enchantable.add(Enchantment.LOOT_BONUS_BLOCKS);
        enchantable.add(Enchantment.LOOT_BONUS_MOBS); enchantable.add(Enchantment.DIG_SPEED);
    }

    @Override
    public void load() {
        skillConfig.load();
        cooltime = skillConfig.getConfig().getInt("iron.cooltime");
        interval = skillConfig.getConfig().getInt("iron.level_interval");
    }

    @Override
    public void activate(int cost) {
        Player user = player.getPlayer();
        ItemStack item = user.getInventory().getItemInMainHand();

        if (user.hasCooldown(user.getInventory().getItemInMainHand().getType())) {
            int display_cooltime = (int) user.getCooldown(user.getInventory().getItemInMainHand().getType()) / 20;
            user.sendMessage("스킬을 사용하기까지 " + display_cooltime + "초 남았습니다.");
            return;
        }
        user.getInventory().removeItem(user.getInventory().getItemInMainHand());

        Random rand = new Random();
        int randomItemIndex = rand.nextInt(ironTools.size());
        int randomEnchantIndex = rand.nextInt(enchantable.size());

//        ItemStack item = new ItemStack(material);
//        ItemMeta meta = item.getItemMeta();
//        meta.addEnchant(enchantment, lvl, true);
//        item.setItemMeta(meta);

        user.playSound(user.getLocation(), Sound.BLOCK_ANVIL_USE, 0.1f, 1.0f);
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            World w = user.getWorld();
            int count = 0;

            @Override
            public void run() {
                if (count == 3) {
                    w.playSound(user.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0f, 1.0f);
                    user.getInventory().addItem(createItem(cost));

                    Bukkit.getScheduler().cancelTask(taskId);
                }
                Location loc = user.getLocation();
                Vector rightHand = PersonalVector.rotateFunction(new Vector(-0.2, 0.7, 0), loc);
                Vector leftHand = PersonalVector.rotateFunction(new Vector(0.2, 0.7, 0), loc);
                Location rightHandLocation = new Location(w, loc.getX() + rightHand.getX(), loc.getY() + rightHand.getY(), loc.getZ() + rightHand.getZ());
                Location leftHandLocation = new Location(w, loc.getX() + leftHand.getX(), loc.getY() + leftHand.getY(), loc.getZ() + leftHand.getZ());

                w.spawnParticle(Particle.LAVA, rightHandLocation, 10, 0.2, 0.2, 0.2, null);
                w.spawnParticle(Particle.LAVA, leftHandLocation, 10, 0.2, 0.2, 0.2, null);

                count++;

            }
        }, 0, 10);


        user.setCooldown(user.getInventory().getItemInMainHand().getType(), cooltime * 20);
    }

    public ItemStack createItem(int cost) {

        Random rand = new Random();
        int randomItemIndex = rand.nextInt(ironTools.size());
        int randomEnchantIndex = rand.nextInt(enchantable.size());
        Material material = ironTools.get(randomItemIndex);
        Enchantment enchantment = enchantable.get(randomEnchantIndex);
        int lvl = (int) cost / interval;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        if (lvl > 0) {
            meta.addEnchant(enchantment, lvl, true);
            if (cost >= 40) {
//            추가 인챈트
                meta.addEnchant(enchantable.get(rand.nextInt(enchantable.size())), lvl, true);
            }
        }


        item.setItemMeta(meta);

        return item;
    }

}
