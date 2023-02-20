package com.aren.game.state;

import com.aren.game.GameManager;
import com.aren.utils.GamePlayer;
import org.bukkit.ChatColor;

import java.util.List;

public abstract class GameStateBuilder {

    GameManager gameManager = GameManager.getInstance();

    public static GameStateBuilder create(GameState state) {
        GameStateBuilder builder = null;
        List<GamePlayer> users = GameManager.getInstance().getParticipants();
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
        }
        return builder;
    }

    public void activate() {
        changeState();
        castTimer();
        setWorldborder();
        managePlayers();
        activateMessage();
    }

    public void deactivate() {
        closeTimer();
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

    protected abstract void castTimer();
    protected abstract void closeTimer();
    protected abstract void managePlayers();
    protected abstract void changeState();
    protected abstract void setWorldborder();

    protected abstract void activateMessage();
    protected abstract void deactivateMessage();
}
