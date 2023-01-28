package com.aren.events;

import com.aren.skills.DiamondSkill;
import com.aren.skills.IronSkill;
import com.aren.skills.MaterialSkills;
import com.aren.utils.GameManager;
import com.aren.utils.GamePlayer;
import com.aren.utils.SkillManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class eventsListener implements Listener {

    private static SkillManager skillManager = SkillManager.getInstance();
    private static GameManager gm = GameManager.getInstance();

    @EventHandler
    public void rightClickMaterial(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();

        if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {

            GamePlayer player = gm.getGamePlayer(p.getUniqueId());

            switch(p.getInventory().getItemInMainHand().getType()) {
                case DIAMOND:
                    player.getPlayerSkills().get(Material.DIAMOND.name()).activate(player.getPlayer());
                    break;
                case GOLD_INGOT:
                    player.getPlayerSkills().get(Material.GOLD_INGOT.name()).activate(player.getPlayer());
                    break;
                case IRON_INGOT:
                    player.getPlayerSkills().get(Material.IRON_INGOT.name()).activate(player.getPlayer());
                    break;
            }
        }
    }

    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();

        if (player.isBlocking()) {
            onBlocking(player);
        }

    }

    @EventHandler
    public void whenCreateShield(CraftItemEvent event) {
        ItemStack result = event.getRecipe().getResult();

        if (result.getType() == Material.SHIELD) {
            ItemMeta resultMeta = result.getItemMeta();

            if (resultMeta instanceof Damageable) {
                IronSkill skillsHashMap = (IronSkill) skillManager.getSkills().get("IRON_INGOT");
                ((Damageable) resultMeta).setDamage((int) skillsHashMap.getDuration());

            }
            result.setItemMeta(resultMeta);
        }
    }

    public void onBlocking(Player player) {
        if (!player.isBlocking())
            return;

        PotionEffect resistance = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*3, 0, true, false);

        player.addPotionEffect(resistance);
    }
}
