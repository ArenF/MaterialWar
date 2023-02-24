package com.aren.ability.events;

import com.aren.ability.AbilityType;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import com.aren.utils.player.GamePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AbilityListener implements Listener {

    GameManager gameManager = GameManager.getInstance();
    ConfigFile skillConfig = ConfigManager.getInstance().getConfigFile("skillConfig");

    @EventHandler
    public void onActivate(PlayerInteractEvent event) {

        if (gameManager.getState().equals(GameState.WAITING))
            return;

        if (!gameManager.contains(event.getPlayer())) {
            return;
        }
        Player p = event.getPlayer();
        Action a = event.getAction();

        if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
            GamePlayer gamePlayer = gameManager.getParticipant(p.getUniqueId());
            ItemStack item = p.getInventory().getItemInMainHand();
            switch (p.getInventory().getItemInMainHand().getType()) {
                case DIAMOND:
                    if (gamePlayer.getAbility(AbilityType.DIAMOND) == null)
                        return;
                    gamePlayer.getAbility(AbilityType.DIAMOND).activate(item.getAmount());
                    break;
                case IRON_INGOT:
                    if (gamePlayer.getAbility(AbilityType.IRON) == null)
                        return;
                    gamePlayer.getAbility(AbilityType.IRON).activate(item.getAmount());
                    break;
                case GOLD_INGOT:
                    if (gamePlayer.getAbility(AbilityType.GOLD) == null)
                        return;
                    gamePlayer.getAbility(AbilityType.GOLD).activate(item.getAmount());
                    break;
                case COBBLESTONE:
                    if (gamePlayer.getAbility(AbilityType.STONE) == null)
                        return;
                    gamePlayer.getAbility(AbilityType.STONE).activate(item.getAmount());
                    break;
            }
        }
    }

}
