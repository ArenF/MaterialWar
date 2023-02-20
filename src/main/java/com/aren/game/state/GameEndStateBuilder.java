package com.aren.game.state;

import com.aren.utils.GamePlayer;

import java.util.List;

public class GameEndStateBuilder extends GameStateBuilder {

    private List<GamePlayer> players;

    public GameEndStateBuilder(List<GamePlayer> players) {
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
    protected void setWorldborder() {

    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : players) {
            player.getPlayer().sendMessage("게임이 끝났습니다.");
        }
    }

    @Override
    protected void deactivateMessage() {

    }
}
