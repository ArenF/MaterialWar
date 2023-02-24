package com.aren.game.event;

import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import com.aren.game.state.GameStateBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
        if (gameManager.contains(event.getPlayer())) {
            gameManager.getParticipant(event.getPlayer().getUniqueId());
        }
    }

}
