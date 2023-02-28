package com.aren.game.event;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import com.aren.game.state.GameStateBuilder;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.player.PlayerState;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class GameEventListener implements Listener {

    GameManager gameManager = GameManager.getInstance();

    @EventHandler
    public void join(PlayerJoinEvent event) {
        if (gameManager.getTimerBar() == null) {
            return;
        }

        gameManager.getTimerBar().getEventConsumer().accept(event);
    }

    @EventHandler
    public void onInvulnerable(EntityDamageEvent event) {
        if (gameManager.getState().equals(GameState.INVULNERABLE)) {
            if (gameManager.getInvulnerableEventConsumer() == null)
                return;
            gameManager.getInvulnerableEventConsumer().accept(event);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (!gameManager.getState().equals(GameState.STARTING)) {
            return;
        }
        if (!gameManager.contains(event.getPlayer()))
            return;
        GamePlayer player = gameManager.getParticipant(event.getPlayer().getUniqueId());
        player.setStatus(PlayerState.DEAD);
        player.getPlayer().setGameMode(GameMode.SPECTATOR);

        ConfigFile playerConfig = ConfigManager.getInstance().getConfigFile("playerConfig");
        FileConfiguration config = playerConfig.getConfig();
        List<String> alives = config.getStringList(PlayerState.ALIVE.name());
        alives.remove(player.getName());
        config.set(PlayerState.ALIVE.name(), alives);

        List<String> deads = config.getStringList(PlayerState.DEAD.name());
        deads.add(player.getName());
        config.set(PlayerState.DEAD.name(), deads);
        playerConfig.save();
        playerConfig.load();

        if (gameManager.getParticipants().size() - 1 == deads.size()) {
            gameManager.start(GameState.COMPLETED);
        }
        event.setCancelled(true);

    }

}
