package com.aren.game.state;

import com.aren.game.GameManager;
import com.aren.utils.TimerBar;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.player.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameEndStateBuilder extends GameStateBuilder {

    private HashMap<UUID, GamePlayer> players;

    public GameEndStateBuilder(HashMap<UUID, GamePlayer> players) {
        this.players = players;
    }

    @Override
    public TimerBar getTimerBar() {
        return null;
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
        GameManager.getInstance().end();
        for (GamePlayer player : players.values()) {
            player.getPlayer().sendMessage(format("게임이 끝났습니다."));
            if (getWinners().isEmpty()) {
                continue;
            }
            for (GamePlayer winner : getWinners()) {
                player.getPlayer().sendMessage(format("게임의 승자는 &b" + winner.getName() + "&f 입니다."));
            }
        }
    }

    private List<GamePlayer> getWinners() {
        List<GamePlayer> winners = new ArrayList<>();
        for (GamePlayer player : players.values()) {
            if (player.getStatus().equals(PlayerState.WINNER)) {
                winners.add(player);
            }
        }
        return winners;
    }

    @Override
    protected void deactivateMessage() {

    }
}
