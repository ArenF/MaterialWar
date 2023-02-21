package com.aren.game.state;

import com.aren.utils.player.GamePlayer;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameEndStateBuilder extends GameStateBuilder {

    private HashMap<UUID, GamePlayer> players;

    public GameEndStateBuilder(HashMap<UUID, GamePlayer> players) {
        this.players = players;
    }

    @Override
    protected void castTimer() {

    }

    @Override
    protected void closeTimer() {
        setState(GameState.WAITING);
    }

    @Override
    protected void managePlayers() {

    }

    @Override
    protected void changeState() {
        setState(GameState.COMPLETED);
    }

    @Override
    protected void activateWorldBorder() {

    }

    @Override
    protected void deactivateWorldBorder() {

    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : players.values()) {
            player.getPlayer().sendMessage("게임이 끝났습니다.");
        }
    }

    @Override
    protected void deactivateMessage() {

    }
}
