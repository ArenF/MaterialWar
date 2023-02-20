package com.aren.game;

import com.aren.game.state.GameInvulnerableStateBuilder;
import com.aren.game.state.GameStartStateBuilder;
import com.aren.game.state.GameState;
import com.aren.game.state.GameStateBuilder;
import com.aren.utils.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private GameState state = GameState.WAITING;
    private static GameManager INSTANCE = new GameManager();

    private List<GamePlayer> participants = new ArrayList<>();
    private GameStateBuilder gameStateBuilder;
    private Consumer<EntityDamageEvent> invulnerableEventConsumer;

    public GameManager() {}

    public static GameManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }

    public void joinPlayer(Player player) {
        GamePlayer gamePlayer = new GamePlayer(player);
        if (participants.contains(gamePlayer)) {
            sendMessage("&e해당 플레이어는 이미 들어와 있습니다.");
            return;
        }
        sendMessage("&a" + player.getName() + "님이 게임에 들어왔습니다.");
        participants.add(gamePlayer);

    }

    public void start(GameState state) {
        if (gameStateBuilder != null)
            gameStateBuilder.deactivate();

        gameStateBuilder = GameStateBuilder.create(state);
        gameStateBuilder.activate();
    }

    public void stop() {
        gameStateBuilder.deactivate();
    }


    public void end() {

    }

    public void setInvulnerableEventConsumer(Consumer<EntityDamageEvent> event) {
        invulnerableEventConsumer = event;
    }

    public Consumer<EntityDamageEvent> getInvulnerableEventConsumer() {
        return invulnerableEventConsumer;
    }

    public void sendMessage(String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "[&aMaterialWar&f] " + msg));
        }
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return state;
    }

    public List<GamePlayer> getParticipants() {
        return participants;
    }

    public GamePlayer getParticipant(UUID uuid) {
        for (GamePlayer gamePlayer : participants) {
            if (gamePlayer.getUniqueId().equals(uuid))
                return gamePlayer;
        }
        return null;
    }

    public boolean contains(Player player) {
        for (GamePlayer gamePlayer : participants) {
            if (player.getUniqueId().equals(gamePlayer.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

}
