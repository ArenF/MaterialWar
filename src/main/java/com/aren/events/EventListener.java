package com.aren.events;

import com.aren.skills.DiamondSkill;
import com.aren.utils.gameManage.GameManager;
import com.aren.utils.ConfigManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener {

    private static ConfigManager configManager = ConfigManager.getInstance();
    private static GameManager gm = GameManager.getInstance();
    private static EventManager eventManager = EventManager.getInstance();
    private DiamondSkill skill = new DiamondSkill();

    @EventHandler
    public void rightClickMaterial(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Action a = event.getAction();

        if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
            if (p.getInventory().getItemInMainHand().getType() == Material.DIAMOND) {
                skill.activate(p, 0);
            }
        }
    }

    @EventHandler
    public void entityInvincible(EntityDamageEvent event) {
        if (eventManager.getDamageEventConsumer() == null) {
            return;
        }
        eventManager.getDamageEventConsumer().accept(event);
    }

}
