package com.aren.game;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.state.GameState;
import com.aren.game.state.GameStateBuilder;
import com.aren.game.team.GameTeam;
import com.aren.utils.TimerBar;
import com.aren.utils.WorldBarrier;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.player.PlayerState;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private GameState state = GameState.WAITING;
    private static GameManager INSTANCE = new GameManager();
    private final HashMap<UUID, GamePlayer> participants = new HashMap<>();
    private GameStateBuilder gameStateBuilder;
    private Consumer<EntityDamageEvent> invulnerableEventConsumer;
    private GameTeam team = GameTeam.getInstance();

    public GameManager() {}

    public static GameManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameManager();
        }
        return INSTANCE;
    }

    public void joinPlayer(Player player) {
        if (contains(player)) {
            sendMessage("&e해당 플레이어는 이미 들어와 있습니다.");
            return;
        }
        GamePlayer gamePlayer = new GamePlayer(player, PlayerState.ALIVE);
        sendMessage("&a" + player.getName() + "님이 게임에 참가하였습니다.");
        participants.put(player.getUniqueId(), gamePlayer);
    }

    public void leavePlayer(Player player) {
        if (!contains(player)) {
            sendMessage("&e해당 플레이어는 존재하지 않습니다.");
            return;
        }
        sendMessage("&a" + player.getName() + "님이 게임에 나가졌습니다.");
        participants.remove(player.getUniqueId());
    }

    public void start(GameState state) {
        if (gameStateBuilder != null)
            gameStateBuilder.deactivate();

        gameStateBuilder = GameStateBuilder.create(state);
        gameStateBuilder.activate();
    }

    public void stop() {
        if (gameStateBuilder == null) {
            sendMessage("&e게임이 시작되지 않았습니다.");
            return;
        }
        gameStateBuilder.deactivate();
    }


    public void end() {

    }

    public TimerBar getTimerBar() {
        if (gameStateBuilder == null)
            return null;

        return gameStateBuilder.getTimerBar();
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

    public HashMap<UUID, GamePlayer> getParticipants() {
        return participants;
    }

    public GamePlayer getParticipant(UUID uuid) {
        return participants.get(uuid);
    }

    public boolean contains(Player player) {
        return participants.containsKey(player.getUniqueId());
    }

}
