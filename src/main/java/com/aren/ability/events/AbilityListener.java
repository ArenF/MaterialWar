package com.aren.ability.events;

import com.aren.ability.AbilityType;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import com.aren.utils.player.GamePlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.swing.text.Caret;
import java.util.ArrayList;
import java.util.List;

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

        if (p.isSneaking()) {
            return;
        }

        if (a.equals(Action.RIGHT_CLICK_AIR) || a.equals(Action.RIGHT_CLICK_BLOCK)) {
            GamePlayer gamePlayer = gameManager.getParticipant(p.getUniqueId());
            ItemStack item = p.getInventory().getItemInMainHand();
            List<Material> materials = new ArrayList<>();
            materials.add(Material.DIAMOND); materials.add(Material.IRON_INGOT);
            materials.add(Material.GOLD_INGOT); materials.add(Material.COBBLESTONE);
            materials.add(Material.COAL); materials.add(Material.EMERALD);
            materials.add(Material.REDSTONE); materials.add(Material.LAPIS_LAZULI);

            for (Material mat : materials) {
                if (!p.getInventory().getItemInMainHand().getType().equals(mat))
                    continue;
                if (transferTypes(mat) == null)
                    continue;
                if (gamePlayer.getAbility(transferTypes(mat)) == null)
                    continue;
                gamePlayer.getAbility(transferTypes(mat)).activate(item.getAmount());
                event.setCancelled(true);
                break;
            }
        }
    }

    private AbilityType transferTypes(Material material) {
        switch (material) {
            case DIAMOND:
                return AbilityType.DIAMOND;
            case IRON_INGOT:
                return AbilityType.IRON;
            case GOLD_INGOT:
                return AbilityType.GOLD;
            case COBBLESTONE:
                return AbilityType.STONE;
            case COAL:
                return AbilityType.COAL;
            case EMERALD:
                return AbilityType.EMERALD;
            case REDSTONE:
                return AbilityType.REDSTONE;
            case LAPIS_LAZULI:
                return AbilityType.LAPIS;
            default:
                return null;
        }

    }

}
