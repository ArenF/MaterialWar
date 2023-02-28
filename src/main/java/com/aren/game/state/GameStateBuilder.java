package com.aren.game.state;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.utils.TimerBar;
import com.aren.utils.WorldBarrier;
import com.aren.utils.player.GamePlayer;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class GameStateBuilder {

    private ConfigFile gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");
    GameManager gameManager = GameManager.getInstance();
    protected WorldBarrier worldBarrier = new WorldBarrier(gameConfig.getConfig().getLocation("game.startLocation"));

    public static GameStateBuilder create(GameState state) {
        GameStateBuilder builder = null;
        HashMap<UUID, GamePlayer> users = GameManager.getInstance().getParticipants();
        switch (state) {
            case WAITING:
                builder = null;
                break;
            case STARTING:
                builder = new GameStartStateBuilder(users);
                break;
            case INVULNERABLE:
                builder = new GameInvulnerableStateBuilder(users);
                break;
            case COMPLETED:
                builder = new GameEndStateBuilder(users);
                break;
            case TEST:
                builder = new GameTestStateBuilder(users);
                break;
        }
        return builder;
    }

    public void activate() {
        changeState();
        castTimer();
        managePlayers();
        activateMessage();
        activateWorldBorder();
    }

    public void deactivate() {
        closeTimer();
        deactivateWorldBorder();
        deactivateMessage();
    }

    public void end() {

    }

    public void setState(GameState state) {
        gameManager.setState(state);
    }

    protected String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', "[&aMaterialWar&f] " + msg);
    }

    public abstract TimerBar getTimerBar();

    protected abstract void castTimer();
    protected abstract void closeTimer();
    protected abstract void managePlayers();
    protected abstract void changeState();
    protected abstract void activateWorldBorder();
    protected abstract void deactivateWorldBorder();

    protected abstract void activateMessage();
    protected abstract void deactivateMessage();
}
