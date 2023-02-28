package com.aren.game.state;

import com.aren.ability.AbilityType;
import com.aren.ability.factory.MaterialAbilityFactory;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.utils.TimerBar;
import com.aren.utils.player.GamePlayer;

import java.util.HashMap;
import java.util.UUID;

public class GameTestStateBuilder extends GameStateBuilder {

    private HashMap<UUID, GamePlayer> users;
    private ConfigFile gameConfig;

    public GameTestStateBuilder(HashMap<UUID, GamePlayer> users) {
        this.users = users;
        gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");
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

    }

    @Override
    protected void managePlayers() {
        MaterialAbilityFactory factory = new MaterialAbilityFactory();
//        플레이어들이 사용할 스킬들을 활성화
        for (GamePlayer player: users.values()) {
            for (AbilityType type : AbilityType.values()) {
                player.addAbility(type, factory.createAbility(type, player));
            }
        }
    }

    @Override
    protected void changeState() {

    }

    @Override
    protected void activateWorldBorder() {

    }

    @Override
    protected void deactivateWorldBorder() {

    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : users.values()) {
            player.getPlayer().sendMessage(format("&e테스트가 시작되었습니다."));
        }
    }

    @Override
    protected void deactivateMessage() {
        for (GamePlayer player : users.values()) {
            player.getPlayer().sendMessage(format("&e테스트가 종료되었습니다."));
        }
    }
}
